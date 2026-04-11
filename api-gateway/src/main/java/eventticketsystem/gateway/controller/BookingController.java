package eventticketsystem.gateway.controller;

import eventticketsystem.gateway.adapter.BookingAdapter;
import eventticketsystem.gateway.dto.booking.Booking;
import eventticketsystem.gateway.dto.booking.BookingRequest;
import eventticketsystem.gateway.dto.event.PageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingAdapter bookingAdapter;

    public BookingController(BookingAdapter bookingAdapter) {
        this.bookingAdapter = bookingAdapter;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request,
                                           @RequestHeader("Authorization") String authHeader){
            return ResponseEntity.ok(this.bookingAdapter.createBooking(request, authHeader));
    }

    @GetMapping
    public ResponseEntity<PageDto<Booking>> getAllBookingsForUser(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        return ResponseEntity.ok(this.bookingAdapter.getAllBookingsForUser(userId, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID id){
       this.bookingAdapter.deleteBooking(id);
       return ResponseEntity.noContent().build();
    }
}
