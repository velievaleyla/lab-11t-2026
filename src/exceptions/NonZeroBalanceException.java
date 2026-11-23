package exceptions;

public class NonZeroBalanceException extends AccountException {
    public NonZeroBalanceException() {
        super();
    }

    public NonZeroBalanceException(String message) {
        super(message);
    }

    public NonZeroBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
