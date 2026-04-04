package eventticketsystem.event.dto.request;

import eventticketsystem.event.dto.EventCategory;
import eventticketsystem.event.dto.EventStatus;

import java.time.OffsetDateTime;

public record CreateEventRequest(String name, String description, EventCategory category, String location,
                                 OffsetDateTime eventDate, Long price, Integer totalTickets, EventStatus status) {
}
