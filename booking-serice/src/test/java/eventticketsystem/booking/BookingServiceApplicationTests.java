package eventticketsystem.booking;

import eventticketsystem.booking.dto.Booking;
import eventticketsystem.booking.dto.BookingRequest;
import eventticketsystem.booking.dto.BookingStatus;
import eventticketsystem.booking.dto.PageDto;
import eventticketsystem.booking.entity.BookingEntity;
import eventticketsystem.booking.entity.TicketInventoryEntity;
import eventticketsystem.booking.exception.BookingNotExistsException;
import eventticketsystem.booking.exception.TicketInventoryNotFoundException;
import eventticketsystem.booking.exception.TicketsNotAvailableException;
import eventticketsystem.booking.repository.BookingRepository;
import eventticketsystem.booking.repository.TicketInventoryRepository;
import eventticketsystem.booking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class BookingServiceApplicationTests {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TicketInventoryRepository ticketInventoryRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private static final UUID userId = UUID.randomUUID();
    private static final UUID eventId = UUID.randomUUID();
    private static final UUID bookingId = UUID.randomUUID();

    private BookingRequest bookingRequest;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void  setUp (){
        bookingRequest = new BookingRequest(userId, eventId, 4);
    }
    @Test
    public void testCreateBookingShouldCreateBooking() {

        TicketInventoryEntity ticketInventoryEntity = new TicketInventoryEntity();
        ticketInventoryEntity.setEventId(eventId);
        ticketInventoryEntity.setAvailableTickets(200);
        ticketInventoryEntity.setTicketPrice(200L);
        ticketInventoryEntity.setTotalTickets(200);

        Mockito.when(this.ticketInventoryRepository.findById(eventId)).thenReturn(Optional.of(ticketInventoryEntity));


        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity.setBookedTickets(4);
        bookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingEntity.setEventId(eventId);
        bookingEntity.setUserId(userId);

        Mockito.when(this.bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);
        Booking booking = this.bookingService.createBooking(bookingRequest);
        assertEquals(booking.userId(), bookingRequest.userId());
        assertEquals(booking.eventId(), bookingRequest.eventId());
        assertEquals(booking.bookedTickets(), bookingRequest.numberOfTickets());

    }

    @Test
    public void testCreateBookingShouldThrowWhenInventoryNotFound(){
        Mockito.when(this.ticketInventoryRepository.findById(eventId))
                .thenThrow(TicketInventoryNotFoundException.class);

        assertThrowsExactly(TicketInventoryNotFoundException.class,
                () -> this.bookingService.createBooking(bookingRequest));
    }

    @Test
    public void testCreateBookingShouldThrowWhenNoTicketsAvailable() {
        TicketInventoryEntity ticketInventoryEntity = new TicketInventoryEntity();
        ticketInventoryEntity.setEventId(eventId);
        ticketInventoryEntity.setAvailableTickets(3);
        ticketInventoryEntity.setTicketPrice(200L);
        ticketInventoryEntity.setTotalTickets(200);

        Mockito.when(this.ticketInventoryRepository.findById(eventId)).thenReturn(Optional.of(ticketInventoryEntity));

        assertThrowsExactly(TicketsNotAvailableException.class, () -> this.bookingService.createBooking(bookingRequest));

    }

    @Test
    public void testSoftDeleteBookingShouldChangeStatusAndReturnTickets(){
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity.setBookedTickets(4);
        bookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingEntity.setEventId(eventId);
        bookingEntity.setUserId(userId);

        BookingEntity cancelledEntity = new BookingEntity();
        cancelledEntity.setId(bookingId);
        cancelledEntity.setBookedTickets(4);
        cancelledEntity.setStatus(BookingStatus.CANCELLED);
        cancelledEntity.setEventId(eventId);
        cancelledEntity.setUserId(userId);

        TicketInventoryEntity ticketInventoryEntity = new TicketInventoryEntity();
        ticketInventoryEntity.setEventId(eventId);
        ticketInventoryEntity.setAvailableTickets(196);
        ticketInventoryEntity.setTicketPrice(200L);
        ticketInventoryEntity.setTotalTickets(200);

        Mockito.when(this.bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));
        Mockito.when(this.bookingRepository.save(any(BookingEntity.class))).thenReturn(cancelledEntity);
        Mockito.when(this.ticketInventoryRepository.findById(eventId)).thenReturn(Optional.of(ticketInventoryEntity));
        Mockito.when(this.ticketInventoryRepository.save(any(TicketInventoryEntity.class))).thenReturn(ticketInventoryEntity);

        this.bookingService.softDeleteBooking(bookingId);

        assertEquals(BookingStatus.CANCELLED, bookingEntity.getStatus());
        assertEquals(200, ticketInventoryEntity.getAvailableTickets());

    }

    @Test
    public void testSoftDeleteBookingShouldThrowWhenBookingNotExists(){
        Mockito.when(this.bookingRepository.findById(bookingId))
                .thenThrow(new BookingNotExistsException(String.valueOf(bookingId)));

        assertThrowsExactly(BookingNotExistsException.class, () -> this.bookingService.softDeleteBooking(bookingId));
    }

    @Test
    public void testGetAllBookingsForUserShouldReturnBookings() {
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity.setBookedTickets(4);
        bookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingEntity.setEventId(eventId);
        bookingEntity.setUserId(userId);

        BookingEntity bookingEntity2 = new BookingEntity();
        bookingEntity2.setId(bookingId);
        bookingEntity2.setBookedTickets(4);
        bookingEntity2.setStatus(BookingStatus.CONFIRMED);
        bookingEntity2.setEventId(eventId);
        bookingEntity2.setUserId(userId);

        Page<BookingEntity> page = new PageImpl<>(List.of(bookingEntity, bookingEntity2));

        Mockito.when(this.bookingRepository.findAllByUserId(
                eq(userId), any(Pageable.class)
        )).thenReturn(page);

        PageDto<Booking> allBookingsForUser = this.bookingService.getAllBookingsForUser(userId, 0, 30);
        assertNotNull(allBookingsForUser);
        assertEquals(2, allBookingsForUser.content().size());
    }

}
