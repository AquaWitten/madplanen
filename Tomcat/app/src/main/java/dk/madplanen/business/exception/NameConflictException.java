package dk.madplanen.business.exception;

public class NameConflictException extends RuntimeException {

    public NameConflictException(String message) {
        super(message);
    }
}
