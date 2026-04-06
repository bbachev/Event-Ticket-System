package eventticketsystem.booking.exception;

public class TicketInventoryNotFoundException extends RuntimeException {
    public TicketInventoryNotFoundException(String id)
    {
        super("TicketInventory with id %s doesn't exist".formatted(id));
    }
}
