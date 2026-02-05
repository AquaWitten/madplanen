package dk.madplanen.api.exception;

public class RequestBodyException extends RuntimeException {

    public RequestBodyException(String message) {
        super(message);
    }

    public RequestBodyException(String message, Throwable cause) {
        super(message, cause);
    }
}
