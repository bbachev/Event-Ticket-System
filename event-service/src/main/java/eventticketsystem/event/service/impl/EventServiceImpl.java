package eventticketsystem.event.service.impl;

import eventticketsystem.event.dto.request.EventFilterRequest;
import eventticketsystem.event.dto.request.EventRequest;
import eventticketsystem.event.entity.EventEntity;
import eventticketsystem.event.dto.response.EventResponse;
import eventticketsystem.event.exception.EventAlreadyExistsException;
import eventticketsystem.event.exception.EventNotExistsException;
import eventticketsystem.event.mapper.EventMapper;
import eventticketsystem.event.repository.EventRepository;
import eventticketsystem.event.service.EventService;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {
    private static final EventMapper MAPPER = Mappers.getMapper(EventMapper.class);

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventResponse createEvent(EventRequest request) {
        EventEntity eventEntity = MAPPER.toEntity(request);
        try {
            return MAPPER.toModel(this.eventRepository.save(eventEntity));
        } catch (DataIntegrityViolationException e) {
            throw new EventAlreadyExistsException(request.name(), request.eventDate());
        }
    }

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

    @Override
    public Page<EventResponse> getAllEvents(EventFilterRequest filter) {
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
        return this.eventRepository.findAll(spec, pageRequest).map(MAPPER::toModel);
    }

    @Override
    public Optional<EventResponse> getEvent(UUID id) {
        return Optional.ofNullable(
                MAPPER.toModel(eventRepository.findById(id)
                        .orElseThrow(() -> new EventNotExistsException(String.valueOf(id)))
                )
        );
    }
}
