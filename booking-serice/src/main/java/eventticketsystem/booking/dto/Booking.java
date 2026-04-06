package eventticketsystem.booking.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Booking (
        UUID id,
        UUID userId,
        UUID eventId,
        Integer bookedTickets,
        Long totalPrice,
        BookingStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
){}
