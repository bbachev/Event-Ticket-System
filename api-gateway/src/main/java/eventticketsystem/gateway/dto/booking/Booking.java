package eventticketsystem.gateway.dto.booking;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Booking (
        UUID id,
        UUID userId,
        UUID eventId,
        Integer bookedTickets,
        BookingStatus status,
        Long totalPrice,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
){}
