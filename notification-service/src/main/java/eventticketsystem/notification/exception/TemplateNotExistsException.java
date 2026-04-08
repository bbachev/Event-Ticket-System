package eventticketsystem.notification.exception;

public class TemplateNotExistsException extends RuntimeException {
    public TemplateNotExistsException(String type) {
        super("Template with id %s does not exist".formatted(type));
    }
}
