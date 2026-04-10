package eventticketsystem.booking.dto;

import java.util.UUID;

public record BookingRequest(UUID userId, UUID eventId, Integer numberOfTickets) {
}
