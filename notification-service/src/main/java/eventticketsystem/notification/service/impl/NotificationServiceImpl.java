package eventticketsystem.notification.service.impl;

import eventticketsystem.notification.exception.TemplateNotExistsException;
import eventticketsystem.notification.dto.MessageTemplate;
import eventticketsystem.notification.dto.NotificationMessage;
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

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);
    private final EmailSender emailSender;
    private final MessageTemplateRepository messageTemplateRepository;
    private final MessageRepository messageRepository;

    public NotificationServiceImpl(EmailSender emailSender, MessageTemplateRepository messageTemplateRepository,
                                   MessageRepository messageRepository) {
        this.emailSender = emailSender;
        this.messageTemplateRepository = messageTemplateRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public NotificationMessage addMessage(NotificationMessage message) {

        MessageTemplateEntity messageTemplateEntity = this.messageTemplateRepository
                .findById(message.templateId())
                .orElseThrow(() -> new TemplateNotExistsException(message.templateId()));

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(message.messageId());
        messageEntity.setSender(emailSender.getSender());
        messageEntity.setTemplateId(message.templateId());
        messageEntity.setReceiver("bbachev29@gmail.com");
        messageEntity.setSubject(messageTemplateEntity.getSubject());
        messageEntity.setCreatedAt(OffsetDateTime.now());

        if (this.messageRepository.existsById(messageEntity.getId())) {
            throw new MessageAlreadyExistsException(messageEntity.getId().toString());
        }

        this.messageRepository.save(messageEntity);

        this.sendMessage(
                new MessageTemplate(
                        messageEntity.getSubject(),
                        messageTemplateEntity.getBodyHtml(),
                        message.toPlaceholders()),
                "");

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
