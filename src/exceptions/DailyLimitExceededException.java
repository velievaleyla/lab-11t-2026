package exceptions;

public class DailyLimitExceededException extends AccountException {
    public DailyLimitExceededException() {
        super();
    }

    public DailyLimitExceededException(String message) {
        super(message);
    }

    public DailyLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
