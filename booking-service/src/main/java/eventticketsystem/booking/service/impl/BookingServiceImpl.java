package eventticketsystem.booking.service.impl;

import eventticketsystem.booking.dto.*;
import eventticketsystem.booking.entity.BookingEntity;
import eventticketsystem.booking.entity.TicketInventoryEntity;
import eventticketsystem.booking.exception.BookingNotExistsException;
import eventticketsystem.booking.exception.TicketInventoryAlreadyExistsException;
import eventticketsystem.booking.exception.TicketInventoryNotFoundException;
import eventticketsystem.booking.exception.TicketsNotAvailableException;
import eventticketsystem.booking.mapper.BookingMapper;
import eventticketsystem.booking.repository.BookingRepository;
import eventticketsystem.booking.repository.TicketInventoryRepository;
import eventticketsystem.booking.service.BookingService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private static final BookingMapper MAPPER = Mappers.getMapper(BookingMapper.class);

    @Value("${spring.kafka.producer.topics.event-updated}")
    private String eventUpdateTopic;

    @Value("${spring.kafka.producer.topics.booking-created}")
    private String bookingCreatedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BookingRepository bookingRepository;
    private final TicketInventoryRepository ticketInventoryRepository;

    public BookingServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, BookingRepository bookingRepository, TicketInventoryRepository ticketInventoryRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.bookingRepository = bookingRepository;
        this.ticketInventoryRepository = ticketInventoryRepository;
    }

    @Transactional
    @Override
    public Booking createBooking(BookingRequest request, UUID userId,  String email) {
        TicketInventoryEntity ticketInventoryEntity =
                this.ticketInventoryRepository.findById(request.eventId())
                        .orElseThrow(() -> new TicketInventoryNotFoundException(String.valueOf(request.eventId())));

        int availableTickets = ticketInventoryEntity.getAvailableTickets();
        if (availableTickets < request.numberOfTickets()) throw new TicketsNotAvailableException();


        ticketInventoryEntity.setAvailableTickets(availableTickets - request.numberOfTickets());
        this.ticketInventoryRepository.save(ticketInventoryEntity);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setUserId(userId);
        bookingEntity.setBookedTickets(request.numberOfTickets());
        bookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingEntity.setEventId(request.eventId());
        bookingEntity.setTotalPrice(request.numberOfTickets() * ticketInventoryEntity.getTicketPrice());
        bookingEntity.setCreatedAt(OffsetDateTime.now());

        Booking model = MAPPER.toModel(this.bookingRepository.save(bookingEntity), email);
        if (ticketInventoryEntity.getAvailableTickets() == 0) {
            this.kafkaTemplate.send(eventUpdateTopic, new EventUpdateMessage(
                    ticketInventoryEntity.getEventId(),
                    EventStatus.SOLD_OUT)
            );
        }

        this.kafkaTemplate.send(bookingCreatedTopic, MAPPER.toMessage(model));
        return model;
    }

    @Override
    public PageDto<Booking> getAllBookingsForUser(UUID userId, int page, int size) {
        Page<BookingEntity> pageResponse = this.bookingRepository.findAllByUserId(userId, PageRequest.of(page, size));
        return new PageDto<>(
                MAPPER.mapBookings(pageResponse.getContent()),
                pageResponse.getNumber(),
                pageResponse.getSize(),
                pageResponse.getTotalElements()
        );
    }

    @Transactional
    @Override
    public void softDeleteBooking(UUID bookingId) {
        BookingEntity booking = this.bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotExistsException(String.valueOf(bookingId)));

        booking.setStatus(BookingStatus.CANCELLED);
        this.bookingRepository.save(booking);

        TicketInventoryEntity ticketInventoryEntity = this.ticketInventoryRepository.findById(booking.getEventId())
                .orElseThrow(() -> new TicketInventoryNotFoundException(String.valueOf(booking.getEventId())));

        int availableTicketsBefore = ticketInventoryEntity.getAvailableTickets();
        int availableTicketsAfter = availableTicketsBefore + booking.getBookedTickets();
        ticketInventoryEntity.setAvailableTickets(availableTicketsAfter);

        if (availableTicketsBefore == 0 && availableTicketsAfter > 0) {
            this.kafkaTemplate.send(this.eventUpdateTopic,
                    new EventUpdateMessage(ticketInventoryEntity.getEventId(), EventStatus.ACTIVE));
        }

        ticketInventoryRepository.save(ticketInventoryEntity);
    }

    @Transactional
    @Override
    public void addToTicketInventory(EventCreatedMessage message) {
        if (this.ticketInventoryRepository.findById(message.eventId()).isPresent()) {
            throw new TicketInventoryAlreadyExistsException(String.valueOf(message.eventId()));
        }

        TicketInventoryEntity ticketInventoryEntity = new TicketInventoryEntity();
        ticketInventoryEntity.setEventId(message.eventId());
        ticketInventoryEntity.setTotalTickets(message.totalTickets());
        ticketInventoryEntity.setTicketPrice(message.ticketPrice());
        ticketInventoryEntity.setAvailableTickets(message.totalTickets());

        this.ticketInventoryRepository.save(ticketInventoryEntity);
    }
}
