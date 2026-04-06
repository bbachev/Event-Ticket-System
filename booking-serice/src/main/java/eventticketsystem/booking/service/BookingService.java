package eventticketsystem.booking.service;

import eventticketsystem.booking.dto.*;

import java.util.UUID;

public interface BookingService {

    Booking createBooking(BookingRequest request);
    PageDto<Booking> getAllBookingsForUser(UUID userId, int page, int size);
    void softDeleteBooking(UUID bookingId);
    void addToTicketInventory(EventCreatedMessage message);
}
