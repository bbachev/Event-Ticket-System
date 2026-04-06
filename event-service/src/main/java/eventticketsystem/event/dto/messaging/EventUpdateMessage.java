package eventticketsystem.event.dto.messaging;

import eventticketsystem.event.dto.EventStatus;

import java.util.UUID;

public record EventUpdateMessage(UUID eventId, EventStatus status) {
}
