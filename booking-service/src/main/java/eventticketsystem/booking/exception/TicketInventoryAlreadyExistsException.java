package eventticketsystem.booking.exception;

public class TicketInventoryAlreadyExistsException extends RuntimeException {
    public TicketInventoryAlreadyExistsException(String eventId) {
        super("TicketInventory with eventId %s already exists".formatted(eventId));
    }
}
