package eventticketsystem.event.service;

import eventticketsystem.event.dto.request.CreateEventRequest;
import eventticketsystem.event.dto.response.EventResponse;

public interface EventService {
    EventResponse createEvent(CreateEventRequest request);
}
