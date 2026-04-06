package eventticketsystem.booking.exception;

public class TicketsNotAvailableException extends RuntimeException {
    public TicketsNotAvailableException() {
        super("Not enough available tickets");
    }
}
