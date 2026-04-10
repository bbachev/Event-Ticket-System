package eventticketsystem.gateway.adapter;

import eventticketsystem.gateway.dto.event.EventFilterRequest;
import eventticketsystem.gateway.dto.event.EventRequest;
import eventticketsystem.gateway.dto.event.EventResponse;
import eventticketsystem.gateway.dto.event.PageDto;

import java.util.Optional;
import java.util.UUID;

public interface EventAdapter {

    EventResponse createEvent(EventRequest request, String auth);
    EventResponse updateEvent(UUID eventId, EventRequest request, String auth);
    void deleteEvent(UUID eventId, String auth);
    Optional<EventResponse> getEvent(UUID eventId, String auth);
    PageDto<EventResponse> getAllEvents(EventFilterRequest filter, String auth);

}
