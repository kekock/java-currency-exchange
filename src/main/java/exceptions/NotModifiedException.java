package exceptions;

public class NotModifiedException extends RuntimeException {
    public NotModifiedException() {
        super();
    }

    public NotModifiedException(String message) {
        super(message);
    }
}
