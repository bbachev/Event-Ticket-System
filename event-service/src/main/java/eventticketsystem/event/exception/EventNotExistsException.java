package eventticketsystem.event.exception;

public class EventNotExistsException extends RuntimeException {
    public EventNotExistsException(String id, String name) {
        super("Event with id '%s' and name '%s' doesn't exist".formatted(id, name));
    }
    public EventNotExistsException(String id) {
        super("Event with id '%s' doesn't exist".formatted(id));
    }
}
