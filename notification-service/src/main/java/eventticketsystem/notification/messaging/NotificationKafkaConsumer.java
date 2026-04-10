package eventticketsystem.notification.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eventticketsystem.notification.dto.BookingCreatedMessage;
import eventticketsystem.notification.dto.EventCreatedMessage;
import eventticketsystem.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationKafkaConsumer {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public NotificationKafkaConsumer(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "event-created")
    public void consume(EventCreatedMessage message) {
        try {
            log.info("Received message in topic: {} message: {}", "event-created", message);
            this.notificationService.handleMessage(message);
        } catch (RuntimeException e){
            log.error(e.getMessage());
        }
    }

    @KafkaListener(topics = "booking-created", containerFactory = "bookingKafkaListenerContainerFactory")
    public void consume(String message) {
        try {
            BookingCreatedMessage booking = objectMapper.readValue(message, BookingCreatedMessage.class);
            log.info("Received message in topic: {} message: {}", "booking-created", message);
            this.notificationService.handleMessage(booking);
        } catch (RuntimeException | JsonProcessingException e){
            log.error(e.getMessage());
        }
    }
}
