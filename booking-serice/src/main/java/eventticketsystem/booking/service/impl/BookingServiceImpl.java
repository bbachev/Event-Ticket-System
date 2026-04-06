package eventticketsystem.booking.service.impl;

import eventticketsystem.booking.dto.Booking;
import eventticketsystem.booking.dto.BookingRequest;
import eventticketsystem.booking.dto.BookingStatus;
import eventticketsystem.booking.dto.PageDto;
import eventticketsystem.booking.entity.BookingEntity;
import eventticketsystem.booking.entity.TicketInventoryEntity;
import eventticketsystem.booking.exception.BookingNotExistsException;
import eventticketsystem.booking.exception.TicketInventoryNotFoundException;
import eventticketsystem.booking.exception.TicketsNotAvailableException;
import eventticketsystem.booking.mapper.BookingMapper;
import eventticketsystem.booking.repository.BookingRepository;
import eventticketsystem.booking.repository.TicketInventoryRepository;
import eventticketsystem.booking.service.BookingService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private static final BookingMapper MAPPER = Mappers.getMapper(BookingMapper.class);

    private final BookingRepository bookingRepository;
    private final TicketInventoryRepository ticketInventoryRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, TicketInventoryRepository ticketInventoryRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketInventoryRepository = ticketInventoryRepository;
    }

    @Transactional
    @Override
    public Booking createBooking(BookingRequest request) {
        TicketInventoryEntity ticketInventoryEntity =
                this.ticketInventoryRepository.findByEventId(request.eventId())
                        .orElseThrow(() -> new TicketInventoryNotFoundException(String.valueOf(request.eventId())));

        int availableTickets = ticketInventoryEntity.getAvailableTickets();
        if (availableTickets < request.numberOfTickets()) throw new TicketsNotAvailableException();


        ticketInventoryEntity.setAvailableTickets(availableTickets - request.numberOfTickets());
        this.ticketInventoryRepository.save(ticketInventoryEntity);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setUserId(request.userId());
        bookingEntity.setBookedTickets(request.numberOfTickets());
        bookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingEntity.setEventId(request.eventId());
        bookingEntity.setTotalPrice(request.numberOfTickets() * ticketInventoryEntity.getTicketPrice());

        return MAPPER.toModel(this.bookingRepository.save(bookingEntity));
    }

    @Override
    public PageDto<Booking> getAllBookingsForUser(UUID userId, int page, int size) {
        Page<BookingEntity> pageResponse = this.bookingRepository.findAllByUserIdAndStatus(userId,
                BookingStatus.CONFIRMED ,PageRequest.of(page, size));
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

        TicketInventoryEntity ticketInventoryEntity = this.ticketInventoryRepository.findByEventId(booking.getEventId())
                .orElseThrow(() -> new TicketInventoryNotFoundException(String.valueOf(booking.getEventId())));

        ticketInventoryEntity.setAvailableTickets(ticketInventoryEntity.getAvailableTickets() + booking.getBookedTickets());

        ticketInventoryRepository.save(ticketInventoryEntity);
    }
}
