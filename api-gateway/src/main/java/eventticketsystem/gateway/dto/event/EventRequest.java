package eventticketsystem.gateway.dto.event;

import java.time.OffsetDateTime;

public record EventRequest(String name, String description, EventCategory category, String location,
                           OffsetDateTime eventDate, Long price, Integer totalTickets, EventStatus status) {
}
