package eventticketsystem.gateway.adapter;

import eventticketsystem.gateway.dto.booking.Booking;
import eventticketsystem.gateway.dto.booking.BookingRequest;
import eventticketsystem.gateway.dto.event.PageDto;

import java.util.UUID;

public interface BookingAdapter {
    Booking createBooking(BookingRequest request, String authHeader);
    PageDto<Booking> getAllBookingsForUser(UUID userId, int page, int size);
    void deleteBooking(UUID id);
}
