package eventticketsystem.event.dto.messaging;

import eventticketsystem.event.dto.EventCategory;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventCreatedMessage(UUID messageId, UUID eventId, Integer totalTickets, EventCategory category, Long ticketPrice,
                                  String name, String description, String location, OffsetDateTime eventDate) {
}
