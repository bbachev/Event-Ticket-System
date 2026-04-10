package eventticketsystem.gateway.exception;

public class WrongUsernameOrPasswordException extends RuntimeException {
    public WrongUsernameOrPasswordException() {
        super("Username and/or password is wrong");
    }
}
