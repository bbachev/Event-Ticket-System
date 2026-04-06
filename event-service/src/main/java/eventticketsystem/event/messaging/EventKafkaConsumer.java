package eventticketsystem.event.messaging;

import eventticketsystem.event.dto.messaging.EventUpdateMessage;
import eventticketsystem.event.exception.EventNotExistsException;
import eventticketsystem.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventKafkaConsumer {
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final EventService eventService;

    public EventKafkaConsumer(EventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(topics = "event-updated")
    public void consume(EventUpdateMessage message) {
        try {
            log.info("Received message in topic: {} message: {}", "event-updated", message);
            this.eventService.updateEventStatus(message);
        } catch (EventNotExistsException e){
            log.error("Event not found: {}", message.eventId());
        }
    }
}
