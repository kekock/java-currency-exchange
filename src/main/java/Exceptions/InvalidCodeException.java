package Exceptions;

public class InvalidCodeException extends RuntimeException {
    public InvalidCodeException() {
        super("Invalid or missing code.");
    }

    public InvalidCodeException(String message) {
        super(message);
    }
}