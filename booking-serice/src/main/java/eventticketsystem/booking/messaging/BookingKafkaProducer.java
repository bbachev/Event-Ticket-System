package eventticketsystem.booking.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BookingKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Object message) {
        log.info("Sending message to topic: {} message: {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
