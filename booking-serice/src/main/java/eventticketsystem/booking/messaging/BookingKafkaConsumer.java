package eventticketsystem.booking.messaging;

import eventticketsystem.booking.dto.EventCreatedMessage;
import eventticketsystem.booking.exception.TicketInventoryAlreadyExistsException;
import eventticketsystem.booking.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingKafkaConsumer {
    private final BookingService bookingService;

    public BookingKafkaConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "event-created")
    public void consume(EventCreatedMessage message) {
        try {
            log.info("Received message in topic: {} message: {}", "event-created", message);
            this.bookingService.addToTicketInventory(message);
        } catch (TicketInventoryAlreadyExistsException e){
            log.error(String.valueOf(e));
        }
    }
}
