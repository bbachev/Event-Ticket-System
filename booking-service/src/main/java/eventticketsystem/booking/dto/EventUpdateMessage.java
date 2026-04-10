package eventticketsystem.booking.dto;

import java.util.UUID;

public record EventUpdateMessage(UUID eventId, EventStatus status) {
}
