package exceptions;

public class TimeSpanException extends RuntimeException {
    public TimeSpanException() {
        super();
    }

    public TimeSpanException(String message) {
        super(message);
    }

    public TimeSpanException(String message, Throwable cause) {
        super(message, cause);
    }
}
