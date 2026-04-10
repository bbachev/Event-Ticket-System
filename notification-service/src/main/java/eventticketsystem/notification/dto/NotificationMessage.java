package eventticketsystem.notification.dto;

import java.util.Map;
import java.util.UUID;

public interface NotificationMessage {
    UUID userId();
    UUID messageId();
    String templateId();
    EventCategory category();
    Map<String, String> toPlaceholders();
    String receiver();
}
