package eventticketsystem.event.controller;

import eventticketsystem.event.dto.request.EventFilterRequest;
import eventticketsystem.event.dto.request.EventRequest;
import eventticketsystem.event.dto.response.EventResponse;
import eventticketsystem.event.exception.EventAlreadyExistsException;
import eventticketsystem.event.exception.EventNotExistsException;
import eventticketsystem.event.service.EventService;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class EventControllerImpl {
    private final EventService eventService;

    public EventControllerImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
        } catch (EventAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("events")
    public ResponseEntity<Page<EventResponse>> getAllEvents(@ModelAttribute EventFilterRequest filter) {
        return ResponseEntity.ok(this.eventService.getAllEvents(filter));
    }

    @GetMapping("events/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable UUID id) {
       return eventService.getEvent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("events/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable UUID id, @RequestBody EventRequest request) {
       try {
           return ResponseEntity.ok(this.eventService.updateEvent(id, request));
       } catch (EventNotExistsException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
    }

    @DeleteMapping("events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        try {
            this.eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (EventNotExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
