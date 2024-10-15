package exceptions;

public class SameCodeException extends RuntimeException {
    public SameCodeException() {
        super();
    }

    public SameCodeException(String message) {
        super(message);
    }
}