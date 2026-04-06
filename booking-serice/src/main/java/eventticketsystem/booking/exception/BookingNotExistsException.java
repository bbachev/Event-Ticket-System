package eventticketsystem.booking.exception;

public class BookingNotExistsException extends RuntimeException {
    public BookingNotExistsException(String id) {
        super("Booking with %s id doesn't exist".formatted(id));
    }
}
