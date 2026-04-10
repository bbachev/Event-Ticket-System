package eventticketsystem.gateway.controller;

import eventticketsystem.gateway.adapter.EventAdapter;
import eventticketsystem.gateway.dto.event.EventFilterRequest;
import eventticketsystem.gateway.dto.event.EventRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("events")
@RestController
public class EventController {
    private final EventAdapter eventAdapter;

    public EventController(EventAdapter eventAdapter) {
        this.eventAdapter = eventAdapter;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventRequest request,
                       @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(this.eventAdapter.createEvent(request, authHeader));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id, @RequestBody EventRequest request,
                                         @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(this.eventAdapter.updateEvent(id, request, authHeader));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        this.eventAdapter.deleteEvent(id, authHeader);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id})")
    public ResponseEntity<?> getEvent(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(this.eventAdapter.getEvent(id, authHeader));
    }

    @GetMapping()
    public ResponseEntity<?> getEvents(@RequestBody EventFilterRequest request, @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(this.eventAdapter.getAllEvents(request, authHeader));
    }
}
