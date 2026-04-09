package eventticketsystem.preference.exception;

import java.util.UUID;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(UUID id) {
        super("User with id %s already exists");
    }
}
