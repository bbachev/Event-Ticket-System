package eventticketsystem.notification.dto;

import java.util.Map;

public record MessageTemplate(String subject, String bodyHtml, Map<String, String> placeholders) {
}
