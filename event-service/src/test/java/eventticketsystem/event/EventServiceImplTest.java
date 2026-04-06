package eventticketsystem.event;

import eventticketsystem.event.dto.EventStatus;
import eventticketsystem.event.dto.request.EventRequest;
import eventticketsystem.event.dto.response.EventResponse;
import eventticketsystem.event.entity.EventEntity;
import eventticketsystem.event.exception.EventAlreadyExistsException;
import eventticketsystem.event.exception.EventNotExistsException;
import eventticketsystem.event.messaging.EventKafkaProducer;
import eventticketsystem.event.repository.EventRepository;
import eventticketsystem.event.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventServiceImpl service;


    @Test
    void contextLoads() {
    }

    @Test
    public void testGetEventShouldReturnEvent(){
        UUID id = UUID.randomUUID();
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(id);

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.of(eventEntity));

        Optional<EventResponse> serviceEvent = this.service.getEvent(id);

        assertTrue(serviceEvent.isPresent());
        assertEquals(eventEntity.getId(), serviceEvent.get().id());
    }

    @Test
    public void testGetEventByIdShouldThrowWhenIdNotExists() {
        UUID id = UUID.randomUUID();
        Mockito.when(this.repository.findById(id)).thenReturn(Optional.empty());

        assertThrowsExactly(EventNotExistsException.class, () -> this.service.getEvent(id));

    }

    @Test
    public void createEventShouldCreateEvent() {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(UUID.randomUUID());
        eventEntity.setName("Test");
        eventEntity.setStatus(EventStatus.ACTIVE);
        eventEntity.setLocation("Sofia");
        eventEntity.setPrice(20000L);

        Mockito.when(this.repository.save(any(EventEntity.class))).thenReturn(eventEntity);

        EventRequest eventRequestService = new EventRequest("Test",
                null, null, "Sofia", OffsetDateTime.now(),
                20000L, 200, EventStatus.ACTIVE);

        EventResponse serviceEvent = this.service.createEvent(eventRequestService);

        assertEquals(serviceEvent.id(), eventEntity.getId());
        assertEquals(serviceEvent.name(), eventEntity.getName());
        assertEquals(serviceEvent.status(), eventEntity.getStatus());
        assertEquals(serviceEvent.price(), eventEntity.getPrice());

    }

    @Test
    public void createEventShouldThrowWhenEventAlreadyExists() {
        Mockito.when(this.repository.save(any(EventEntity.class))).thenThrow(EventAlreadyExistsException.class);

        EventRequest eventRequestService = new EventRequest("Test",
                null, null, "Sofia", OffsetDateTime.now(),
                20000L, 200, EventStatus.ACTIVE);

        assertThrowsExactly(EventAlreadyExistsException.class, () -> this.service.createEvent(eventRequestService));
    }

    @Test
    public void updateEventShouldUpdateEvent() {
        EventRequest eventRequest = new EventRequest("Test",
                null, null, "Sofia", OffsetDateTime.now(),
                20000L, 200, EventStatus.ACTIVE);
        UUID id = UUID.randomUUID();


        EventEntity preUpdate = new EventEntity();
        preUpdate.setId(id);
        preUpdate.setName("Test2");
        preUpdate.setLocation("Plovdiv");
        preUpdate.setPrice(10000L);
        preUpdate.setTotalTickets(250);

        EventEntity postUpdate = new EventEntity();
        postUpdate.setId(id);
        postUpdate.setName(eventRequest.name());
        postUpdate.setLocation(eventRequest.location());
        postUpdate.setPrice(eventRequest.price());
        postUpdate.setTotalTickets(200);

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.of(preUpdate));
        Mockito.when(this.repository.save(any(EventEntity.class))).thenReturn(postUpdate);

        EventResponse eventResponse = this.service.updateEvent(id, eventRequest);

        assertEquals(eventResponse.id(), id);
        assertEquals(eventResponse.name(), eventRequest.name());
        assertEquals(eventResponse.location(), eventRequest.location());
        assertEquals(eventResponse.price(), eventRequest.price());
        assertEquals(eventResponse.totalTickets(), eventRequest.totalTickets());
    }

    @Test
    public void updateEventShouldThrowWhenEventNotExists() {
        UUID id = UUID.randomUUID();
        EventRequest request = new EventRequest("Test", null, null, "Sofia",
                OffsetDateTime.now(), 20000L, 200, EventStatus.ACTIVE);

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.empty());

        assertThrowsExactly(EventNotExistsException.class, () -> this.service.updateEvent(id, request));
    }

    @Test
    public void deleteEventShouldDeleteEvent() {
        UUID id = UUID.randomUUID();
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(id);

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.of(eventEntity));
        this.service.deleteEvent(id);
        Mockito.verify(this.repository).delete(eventEntity);
    }

    @Test
    public void deleteEventShouldThrowWhenDeleteEvent() {
        UUID id = UUID.randomUUID();
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(id);

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.empty());
        assertThrowsExactly(EventNotExistsException.class, () -> this.service.deleteEvent(id));
    }


    @Test
    public void createEventShouldSendKafkaMessage() {
        EventRequest eventRequestService = new EventRequest("Test",
                null, null, "Sofia", OffsetDateTime.now(),
                20000L, 200, EventStatus.ACTIVE);

        this.service.createEvent(eventRequestService);

        Mockito.verify(kafkaTemplate).send(any(), any());
    }
}
