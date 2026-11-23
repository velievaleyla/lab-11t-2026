package exceptions;

public class IllegalScaleFactorException extends TimeSpanException {
    public IllegalScaleFactorException() {
        super();
    }

    public IllegalScaleFactorException(String message) {
        super(message);
    }

    public IllegalScaleFactorException(String message, Throwable cause) {
        super(message, cause);
    }
}
