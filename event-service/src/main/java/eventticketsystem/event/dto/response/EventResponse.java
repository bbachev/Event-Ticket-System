package eventticketsystem.event.dto.response;

import eventticketsystem.event.dto.EventCategory;
import eventticketsystem.event.dto.EventStatus;

import java.time.OffsetDateTime;

public record EventResponse(String name, String description, EventCategory category, String location,
                            OffsetDateTime eventDate, Long price, Integer totalTickets, EventStatus status,
                            OffsetDateTime updatedAt, OffsetDateTime createdAt) {
}
