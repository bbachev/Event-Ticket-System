package eventticketsystem.notification;

import eventticketsystem.notification.adapter.PreferenceClient;
import eventticketsystem.notification.dto.EventCategory;
import eventticketsystem.notification.dto.EventCreatedMessage;
import eventticketsystem.notification.dto.NotificationMessage;
import eventticketsystem.notification.dto.User;
import eventticketsystem.notification.entity.MessageEntity;
import eventticketsystem.notification.entity.MessageTemplateEntity;
import eventticketsystem.notification.exception.MessageAlreadyExistsException;
import eventticketsystem.notification.exception.TemplateNotExistsException;
import eventticketsystem.notification.messenger.EmailSender;
import eventticketsystem.notification.repository.MessageRepository;
import eventticketsystem.notification.repository.MessageTemplateRepository;
import eventticketsystem.notification.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTests {
    @Mock
    PreferenceClient preferenceClient;

    @Mock
    EmailSender emailSender;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MessageTemplateRepository messageTemplateRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private NotificationMessage notificationMessage;
    private MessageTemplateEntity messageTemplate;

    @BeforeEach
    public void setUp() {
        notificationMessage = new EventCreatedMessage(
                UUID.randomUUID(),
                UUID.randomUUID(),
                200,
                EventCategory.SPORTS,
                1500L,
                "Test",
                "Test",
                "Sofia",
                OffsetDateTime.now()

        );

        messageTemplate = new MessageTemplateEntity();
        messageTemplate.setId("NEW_EVENT");
    }

    @Test
    public void testHandleMessageShouldThrowIfTemplateDoesNotExist(){
        when(this.messageTemplateRepository.findById(any(String.class))).thenThrow(TemplateNotExistsException.class);

        assertThrowsExactly(TemplateNotExistsException.class, () -> this.notificationService.handleMessage(notificationMessage));
    }

    @Test
    public void testHandleMessageShouldThrowIfMessageAlreadyExists() {

        when(this.messageTemplateRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(messageTemplate));

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(notificationMessage.messageId());
        messageEntity.setTemplateId(notificationMessage.templateId());
        messageEntity.setSender("test");
        messageEntity.setSubject(messageTemplate.getSubject());
        messageEntity.setCreatedAt(OffsetDateTime.now());

        User user = new User(UUID.randomUUID(), "test", "test", "test@test.com");

        when(this.preferenceClient.getUsersByPreference(any(EventCategory.class))).thenReturn(List.of(user));
        when(this.messageRepository.existsById(any(UUID.class))).thenThrow(MessageAlreadyExistsException.class);

        assertThrowsExactly(MessageAlreadyExistsException.class, () -> this.notificationService.handleMessage(notificationMessage));
    }

    @Test
    public void testAddMessageShouldAddMessage() {
        when(this.messageTemplateRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(messageTemplate));

        when(this.messageRepository.existsById(any(UUID.class))).thenReturn(false);
        when(this.messageRepository.save(any(MessageEntity.class))).thenReturn(new MessageEntity());
        when(this.emailSender.getSender()).thenReturn("test@test.com");

        User user = new User(UUID.randomUUID(), "test", "test", "test@test.com");

        when(this.preferenceClient.getUsersByPreference(any(EventCategory.class))).thenReturn(List.of(user));

        NotificationMessage result = this.notificationService.handleMessage(notificationMessage);
        assertNotNull(result);
        Mockito.verify(this.messageRepository).save(any(MessageEntity.class));

    }

}
