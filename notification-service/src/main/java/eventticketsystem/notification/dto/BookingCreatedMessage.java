package eventticketsystem.notification.dto;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public record BookingCreatedMessage(
        UUID messageId,
        UUID userId,
        UUID eventId,
        Integer bookedTickets,
        Long totalPrice,
        OffsetDateTime timestamp
        ) implements NotificationMessage{

        @Override
        public String templateId() {
                return TemplateType.BOOKING_CREATED.toString();
        }

        @Override
        public Map<String, String> toPlaceholders() {
                return Map.of(
                        "bookingId", messageId.toString(),
                        "bookedTickets", bookedTickets.toString(),
                        "totalPrice", String.format("%.2f EUR", totalPrice / 100.0),
                        "bookingDate", timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm"))
                );
        }
}
