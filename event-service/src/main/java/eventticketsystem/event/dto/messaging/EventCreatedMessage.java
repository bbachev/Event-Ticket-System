package eventticketsystem.event.dto.messaging;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventCreatedMessage(UUID messageId, UUID eventId, Integer totalTickets, String category,  Long ticketPrice,
                                  String name, String description, String location, OffsetDateTime eventDate) {
}
