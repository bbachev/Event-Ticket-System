package eventticketsystem.booking.controller;

import eventticketsystem.booking.dto.Booking;
import eventticketsystem.booking.dto.BookingRequest;
import eventticketsystem.booking.dto.PageDto;
import eventticketsystem.booking.exception.BookingNotExistsException;
import eventticketsystem.booking.exception.TicketInventoryNotFoundException;
import eventticketsystem.booking.exception.TicketsNotAvailableException;
import eventticketsystem.booking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request,
                                           @RequestHeader("X-User-Id") UUID userId,
                                           @RequestHeader("X-User-Email") String email){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request, userId, email));
        } catch (TicketInventoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TicketsNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("bookings")
    public ResponseEntity<PageDto<Booking>> getAllBookingsForUser(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        return ResponseEntity.ok(this.bookingService.getAllBookingsForUser(userId, page, size));
    }

    @DeleteMapping("bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID id){
        try {
            this.bookingService.softDeleteBooking(id);
            return ResponseEntity.noContent().build();
        } catch (BookingNotExistsException | TicketInventoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
