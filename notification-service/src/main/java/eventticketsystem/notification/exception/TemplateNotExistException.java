package eventticketsystem.notification.exception;

public class TemplateNotExistException extends RuntimeException {
    public TemplateNotExistException(String id) {
        super("Template with %s id does not exists".formatted(id));
    }
}
