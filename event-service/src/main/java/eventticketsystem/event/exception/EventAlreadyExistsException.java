package eventticketsystem.event.exception;

import java.time.OffsetDateTime;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException(String name, OffsetDateTime date) {
        super("Event with name '%s' and date '%s' already exists".formatted(name, date));
    }
}
