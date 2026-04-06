package eventticketsystem.event.dto.messaging;

import java.util.UUID;

public record EventCreatedMessage(UUID messageId, Integer totalTickets, Integer availableTickets, Long ticketPrice) {
}
