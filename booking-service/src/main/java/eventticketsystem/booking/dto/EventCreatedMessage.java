package eventticketsystem.booking.dto;

import java.util.UUID;

public record EventCreatedMessage(UUID eventId, Integer totalTickets, Long ticketPrice) {
}
