package exceptions;

public class DuplicateUserIdException extends AccountException {
    public DuplicateUserIdException() {
        super();
    }

    public DuplicateUserIdException(String message) {
        super(message);
    }

    public DuplicateUserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
