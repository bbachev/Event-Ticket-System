package eventticketsystem.notification.dto;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public record EventCreatedMessage(
        UUID messageId,
        UUID eventId,
        Integer totalTickets,
        EventCategory category,
        String receiver,
        Long ticketPrice,
        String name,
        String description,
        String location,
        OffsetDateTime eventDate
)
        implements NotificationMessage {
    @Override
    public UUID userId() {
        return null;
    }

    @Override
    public String receiver() {
        return null;
    }

    @Override
    public String templateId() {
        return TemplateType.NEW_EVENT.toString();
    }

    @Override
    public Map<String, String> toPlaceholders() {
        return  Map.of(
                "eventName", name,
                "category", category.name(),
                "location", location,
                "eventDate", eventDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm")),
                "price", String.format("%.2f EUR", ticketPrice / 100.0)
        );
    }



}
