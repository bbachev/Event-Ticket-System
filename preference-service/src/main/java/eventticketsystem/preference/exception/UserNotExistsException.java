package eventticketsystem.preference.exception;

import java.util.UUID;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(UUID id) {
        super("User with id = %s does not exist".formatted(id));
    }
}
