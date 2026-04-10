package eventticketsystem.notification.service.impl;

import eventticketsystem.notification.adapter.PreferenceClient;
import eventticketsystem.notification.dto.*;
import eventticketsystem.notification.exception.TemplateNotExistsException;
import eventticketsystem.notification.entity.MessageEntity;
import eventticketsystem.notification.entity.MessageTemplateEntity;
import eventticketsystem.notification.exception.MessageAlreadyExistsException;
import eventticketsystem.notification.mapper.NotificationMapper;
import eventticketsystem.notification.messenger.EmailSender;
import eventticketsystem.notification.repository.MessageRepository;
import eventticketsystem.notification.repository.MessageTemplateRepository;
import eventticketsystem.notification.service.NotificationService;
import jakarta.mail.MessagingException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);
    private final EmailSender emailSender;
    private final MessageTemplateRepository messageTemplateRepository;
    private final MessageRepository messageRepository;
    private final PreferenceClient preferenceClient;

    public NotificationServiceImpl(EmailSender emailSender, MessageTemplateRepository messageTemplateRepository,
                                   MessageRepository messageRepository, PreferenceClient preferenceClient) {
        this.emailSender = emailSender;
        this.messageTemplateRepository = messageTemplateRepository;
        this.messageRepository = messageRepository;
        this.preferenceClient = preferenceClient;
    }

    @Override
    public NotificationMessage handleMessage(NotificationMessage message) {
        MessageTemplateEntity messageTemplateEntity = this.messageTemplateRepository
                .findById(message.templateId())
                .orElseThrow(() -> new TemplateNotExistsException(message.templateId()));

        List<User> receivers;
        if (message.templateId().equals(TemplateType.NEW_EVENT.toString())) {
            receivers = this.preferenceClient.getUsersByPreference(message.category());
        } else {
            receivers = List.of(new User(message.userId(), null, null, message.receiver()));
        }

        receivers.forEach(user -> {
            UUID deterministicId = UUID.nameUUIDFromBytes(
                    (user.id().toString() + message.messageId().toString()).getBytes()
            );

            if (this.messageRepository.existsById(deterministicId)) {
                throw new MessageAlreadyExistsException(deterministicId);
            }

            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setId(deterministicId);
            messageEntity.setSender(emailSender.getSender());
            messageEntity.setTemplateId(message.templateId());
            messageEntity.setReceiver(user.email());
            messageEntity.setSubject(messageTemplateEntity.getSubject());
            messageEntity.setCreatedAt(OffsetDateTime.now());

            this.messageRepository.save(messageEntity);

            this.sendMessage(new MessageTemplate(
                    messageEntity.getSubject(),
                    messageTemplateEntity.getBodyHtml(),
                    message.toPlaceholders()), user.email());
        });

        return message;
    }

    @Override
    public void sendMessage(MessageTemplate messageTemplate, String receiver) {
        try {
            this.emailSender.send(messageTemplate, receiver);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
