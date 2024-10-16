package exceptions;

public class InvalidCodeException extends RuntimeException {
    public InvalidCodeException() {
        super();
    }

    public InvalidCodeException(String message) {
        super(message);
    }
}