package Exceptions;

public class NotModifiedException extends RuntimeException {
    public NotModifiedException() {
        super("Not modified");
    }

    public NotModifiedException(String message) {
        super(message);
    }
}
