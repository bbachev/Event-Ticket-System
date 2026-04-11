package eventticketsystem.gateway.adapter;

import eventticketsystem.gateway.dto.event.EventFilterRequest;
import eventticketsystem.gateway.dto.event.EventRequest;
import eventticketsystem.gateway.dto.event.EventResponse;
import eventticketsystem.gateway.dto.event.PageDto;

import java.util.Optional;
import java.util.UUID;

public interface EventAdapter {

    EventResponse createEvent(EventRequest request);
    EventResponse updateEvent(UUID eventId, EventRequest request);
    void deleteEvent(UUID eventId);
    Optional<EventResponse> getEvent(UUID eventId);
    PageDto<EventResponse> getAllEvents(EventFilterRequest filter);

}
