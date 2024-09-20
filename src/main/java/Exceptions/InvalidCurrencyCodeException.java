package Exceptions;

public class InvalidCurrencyCodeException extends RuntimeException {
    public InvalidCurrencyCodeException() {
        super("Invalid or missing currency code.");
    }

    public InvalidCurrencyCodeException(String message) {
        super(message);
    }
}