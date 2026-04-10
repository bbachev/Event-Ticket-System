package eventticketsystem.gateway.dto.booking;

import java.util.UUID;

public record BookingRequest(UUID userId, UUID eventId, Integer numberOfTickets) {
}
