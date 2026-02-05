package dk.madplanen.persistence.exception;

public class DatabaseException extends RuntimeException {

    public static final String POSTGRES_UNIQUE_VIOLATION = "23505";

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
