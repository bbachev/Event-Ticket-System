package eventticketsystem.event.service.impl;

import eventticketsystem.event.dto.messaging.EventUpdateMessage;
import eventticketsystem.event.dto.request.EventFilterRequest;
import eventticketsystem.event.dto.request.EventRequest;
import eventticketsystem.event.dto.response.PageDto;
import eventticketsystem.event.entity.EventEntity;
import eventticketsystem.event.dto.response.EventResponse;
import eventticketsystem.event.exception.EventAlreadyExistsException;
import eventticketsystem.event.exception.EventNotExistsException;
import eventticketsystem.event.mapper.EventMapper;
import eventticketsystem.event.repository.EventRepository;
import eventticketsystem.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private static final EventMapper MAPPER = Mappers.getMapper(EventMapper.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final EventRepository eventRepository;

    @Value("${spring.kafka.producer.topics.event-created}")
    private String eventCreatedTopic;

    public EventServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, EventRepository eventRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventRepository = eventRepository;
    }

    @CacheEvict(value = "events", allEntries = true)
    @Override
    public EventResponse createEvent(EventRequest request) {
        EventEntity eventEntity = MAPPER.toEntity(request);
        try {
            EventEntity saved = this.eventRepository.save(eventEntity);
            this.kafkaTemplate.send(eventCreatedTopic, MAPPER.toMessageDto(saved));

            return MAPPER.toModel(saved);
        } catch (DataIntegrityViolationException e) {
            throw new EventAlreadyExistsException(request.name(), request.eventDate());
        }
    }

    @CacheEvict(value = "events", allEntries = true)
    @Override
    public EventResponse updateEvent(UUID id, EventRequest request) {
        EventEntity eventEntity = this.eventRepository.findById(id)
                .orElseThrow(() -> new EventNotExistsException(String.valueOf(id), request.name()));

        eventEntity.setName(request.name());
        eventEntity.setDescription(request.description());
        eventEntity.setCategory(request.category());
        eventEntity.setLocation(request.location());
        eventEntity.setEventDate(request.eventDate());
        eventEntity.setPrice(request.price());
        eventEntity.setTotalTickets(request.totalTickets());
        eventEntity.setStatus(request.status());
        eventEntity.setUpdatedAt(OffsetDateTime.now());

        return MAPPER.toModel(eventRepository.save(eventEntity));
    }

    @Cacheable(value = "events", key = "#filter.toCacheKey()")
    @Override
    public PageDto<EventResponse> getAllEvents(EventFilterRequest filter) {
        int from = filter.page() == null ? 0 : filter.page();
        int pageSize = filter.size() == null ? 30 : filter.size();
        Specification<EventEntity> spec = filter.toSpecification();

        Sort sort = Sort.unsorted();

        if (filter.sortBy() != null) {
            sort = filter.sortDir() != null && filter.sortDir().equalsIgnoreCase("DESC")
                    ? Sort.by(filter.sortBy()).descending()
                    : Sort.by(filter.sortBy()).ascending();
        }

        PageRequest pageRequest = PageRequest.of(from, pageSize, sort);
        Page<EventResponse> response = this.eventRepository.findAll(spec, pageRequest).map(MAPPER::toModel);

        return new PageDto<>(
                response.getContent(),
                response.getNumber(),
                response.getSize(),
                response.getTotalElements()
        );
    }

    @Override
    public Optional<EventResponse> getEvent(UUID id) {
        return Optional.ofNullable(
                MAPPER.toModel(eventRepository.findById(id)
                        .orElseThrow(() -> new EventNotExistsException(String.valueOf(id)))
                )
        );
    }

    @CacheEvict(value = "events", allEntries = true)
    @Override
    public void deleteEvent(UUID id) {
        EventEntity entity = this.eventRepository.findById(id)
                .orElseThrow(() -> new EventNotExistsException(String.valueOf(id)));
        this.eventRepository.delete(entity);
    }

    @CacheEvict(value = "events", allEntries = true)
    @Override
    public void updateEventStatus(EventUpdateMessage message) {
        EventEntity eventEntity = this.eventRepository.findById(message.eventId())
                .orElseThrow(() -> new EventNotExistsException(String.valueOf(message.eventId())));

        eventEntity.setStatus(message.status());
        eventEntity.setUpdatedAt(OffsetDateTime.now());

        this.eventRepository.save(eventEntity);

    }
}
