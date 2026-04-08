package eventticketsystem.notification.exception;

public class MessageAlreadyExistsException extends RuntimeException {
    public MessageAlreadyExistsException(String id) {
        super("Message with id %s already exists".formatted(id));
    }
}
