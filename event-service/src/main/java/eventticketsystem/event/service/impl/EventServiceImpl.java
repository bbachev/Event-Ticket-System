package eventticketsystem.event.service.impl;

import eventticketsystem.event.entity.EventEntity;
import eventticketsystem.event.dto.request.CreateEventRequest;
import eventticketsystem.event.dto.response.EventResponse;
import eventticketsystem.event.exception.EventAlreadyExistsException;
import eventticketsystem.event.mapper.EventMapper;
import eventticketsystem.event.repository.EventRepository;
import eventticketsystem.event.service.EventService;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
    private static final EventMapper MAPPER = Mappers.getMapper(EventMapper.class);

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventResponse createEvent(CreateEventRequest request) {
        EventEntity eventEntity = MAPPER.toEntity(request);
        try {
            this.eventRepository.save(eventEntity);
            return MAPPER.toModel(eventEntity);
        } catch (DataIntegrityViolationException e) {
            throw new EventAlreadyExistsException(request.name(), request.eventDate());
        }
    }
}
