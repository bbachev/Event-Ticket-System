package com.search.messaging;


import com.search.document.EventDocument;
import com.search.repository.EventSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class CdcEventConsumer {
    private final EventSearchRepository eventSearchRepository;

    public CdcEventConsumer(EventSearchRepository eventSearchRepository) {
        this.eventSearchRepository = eventSearchRepository;
    }

    @KafkaListener(topics = "cdc.public.event", groupId = "search-service")
    public void consume(Map<String, Object> envelope) {
        if (envelope == null) {
            log.warn("Received null envelope (likely tombstone), skipping");
            return;
        }

        Map<String, Object> payload = (Map<String, Object>) envelope.get("payload");
        if (payload == null) {
            log.warn("Envelope has no 'payload' field, skipping");
            return;
        }

        String op = (String) payload.get("op");

        if ("d".equals(op)) {
            Map<String, Object> before = (Map<String, Object>) payload.get("before");
            if (before == null) {
                log.warn("Delete event with null 'before', skipping");
                return;
            }
            String id = (String) before.get("id");
            eventSearchRepository.deleteById(id);
            return;
        }

        Map<String, Object> after = (Map<String, Object>) payload.get("after");
        if (after == null) {
            log.warn("Non-delete event (op={}) with null 'after', skipping", op);
            return;
        }

        EventDocument doc = new EventDocument();
        doc.setId((String) after.get("id"));
        doc.setName((String) after.get("name"));
        doc.setCategory((String) after.get("category"));
        doc.setEventDate((String) after.get("event_date"));
        doc.setLocation((String) after.get("location"));
        doc.setPrice((Integer) after.get("price"));
        doc.setStatus((String) after.get("status"));
        doc.setDescription((String) after.get("description"));
        doc.setTotalTickets((Integer) after.get("total_tickets"));

        eventSearchRepository.save(doc);
        log.info("Indexed event {} (op={})", doc.getId(), op);
    }

}

