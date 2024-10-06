package Exceptions;

public class SameCodeException extends RuntimeException {
    public SameCodeException() {
        super("Base code equal to target code.");
    }

    public SameCodeException(String message) {
        super(message);
    }
}