package exceptions;

public class IllegalInputTimeSpanException extends TimeSpanException {
    public IllegalInputTimeSpanException() {
        super();
    }

    public IllegalInputTimeSpanException(String message) {
        super(message);
    }

    public IllegalInputTimeSpanException(String message, Throwable cause) {
        super(message, cause);
    }
}
