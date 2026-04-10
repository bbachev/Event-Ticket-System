package eventticketsystem.booking.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BookingCreatedMessage(
        UUID messageId,
        UUID userId,
        UUID eventId,
        String receiver,
        Integer bookedTickets,
        Long totalPrice,
        OffsetDateTime timestamp
        ) {
}
