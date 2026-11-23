package exceptions;

public class NegativeTimeSpanException extends TimeSpanException {
    public NegativeTimeSpanException() {
        super();
    }

    public NegativeTimeSpanException(String message) {
        super(message);
    }

    public NegativeTimeSpanException(String message, Throwable cause) {
        super(message, cause);
    }
}
