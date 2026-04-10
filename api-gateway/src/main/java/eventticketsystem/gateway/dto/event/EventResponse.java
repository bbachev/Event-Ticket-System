package eventticketsystem.gateway.dto.event;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

public record EventResponse(UUID id, String name, String description, EventCategory category, String location,
                            OffsetDateTime eventDate, Long price, Integer totalTickets, EventStatus status,
                            OffsetDateTime updatedAt, OffsetDateTime createdAt) implements Serializable {
}
