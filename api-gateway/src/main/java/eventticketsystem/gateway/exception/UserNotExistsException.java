package eventticketsystem.gateway.exception;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(String message) {
        super("User with email: %s does not exist".formatted(message));
    }
}
