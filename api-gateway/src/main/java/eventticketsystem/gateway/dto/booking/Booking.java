package eventticketsystem.gateway.dto.booking;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Booking (
        UUID id,
        UUID userId,
        UUID eventId,
        Integer bookedTickets,
        Long totalPrice,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
){}
