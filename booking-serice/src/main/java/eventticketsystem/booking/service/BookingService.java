package eventticketsystem.booking.service;

import eventticketsystem.booking.dto.Booking;
import eventticketsystem.booking.dto.BookingRequest;
import eventticketsystem.booking.dto.PageDto;

import java.util.UUID;

public interface BookingService {

    Booking createBooking(BookingRequest request);
    PageDto<Booking> getAllBookingsForUser(UUID userId, int page, int size);
    void softDeleteBooking(UUID bookingId);
}
