package eventticketsystem.notification.exception;

import java.util.UUID;

public class MessageAlreadyExistsException extends RuntimeException {
    public MessageAlreadyExistsException(UUID id) {
        super("Message with id %s already exists".formatted(id));
    }
}
