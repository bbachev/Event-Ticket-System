package eventticketsystem.notification.dto;

import java.util.Map;
import java.util.UUID;

public interface NotificationMessage {
    UUID userId();
    UUID messageId();
    String templateId();
    Map<String, String> toPlaceholders();
}
