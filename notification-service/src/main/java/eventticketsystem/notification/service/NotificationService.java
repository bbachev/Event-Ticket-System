package eventticketsystem.notification.service;

import eventticketsystem.notification.dto.MessageTemplate;
import eventticketsystem.notification.dto.NotificationMessage;

public interface NotificationService {
    NotificationMessage addMessage(NotificationMessage message);
    void sendMessage(MessageTemplate message, String receiver);
}
