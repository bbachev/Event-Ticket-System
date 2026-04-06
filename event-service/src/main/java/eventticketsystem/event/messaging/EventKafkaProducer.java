package eventticketsystem.event.messaging;

import org.springframework.kafka.core.KafkaTemplate;

public class EventKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
