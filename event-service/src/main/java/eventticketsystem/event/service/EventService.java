package eventticketsystem.event.service;

import eventticketsystem.event.dto.messaging.EventUpdateMessage;
import eventticketsystem.event.dto.request.EventFilterRequest;
import eventticketsystem.event.dto.request.EventRequest;
import eventticketsystem.event.dto.response.EventResponse;
import eventticketsystem.event.dto.response.PageDto;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    EventResponse createEvent(EventRequest request);
    EventResponse updateEvent(UUID id, EventRequest request);
    PageDto<EventResponse> getAllEvents(EventFilterRequest filter);
    Optional<EventResponse> getEvent(UUID id);
    void deleteEvent(UUID id);
    void updateEventStatus(EventUpdateMessage message);
}
