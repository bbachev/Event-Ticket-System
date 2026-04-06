package eventticketsystem.event.messaging;

import eventticketsystem.event.dto.messaging.EventUpdateMessage;
import eventticketsystem.event.service.EventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

public class EventKafkaConsumer {
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final EventService eventService;

    public EventKafkaConsumer(EventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(topics = "event-status")
    public void consume(EventUpdateMessage message) {
        this.eventService.updateAvailableTickets(message.eventId(), message);

    }
}
