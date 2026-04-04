package eventticketsystem.event.controller;

import eventticketsystem.event.dto.request.CreateEventRequest;
import eventticketsystem.event.dto.request.UpdateEventRequest;
import eventticketsystem.event.dto.response.EventResponse;
import eventticketsystem.event.exception.EventAlreadyExistsException;
import eventticketsystem.event.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventControllerImpl {
    private final EventService eventService;

    public EventControllerImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events")
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
        } catch (EventAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return null;
    }

    public ResponseEntity<EventResponse> getEventById(@PathVariable String id) {
        return null;
    }

    public ResponseEntity<EventResponse> updateEvent(@PathVariable String id, @RequestBody UpdateEventRequest request) {
        return null;
    }

    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        return null;
    }
}
