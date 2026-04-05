package eventticketsystem.event.service;

import eventticketsystem.event.dto.request.EventFilterRequest;
import eventticketsystem.event.dto.request.EventRequest;
import eventticketsystem.event.dto.response.EventResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    EventResponse createEvent(EventRequest request);
    EventResponse updateEvent(UUID id, EventRequest request);
    Page<EventResponse> getAllEvents(EventFilterRequest filter);
    Optional<EventResponse> getEvent(UUID id);
    void deleteEvent(UUID id);
}
