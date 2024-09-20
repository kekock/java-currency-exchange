package Exceptions;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException() {
        super("Currency not found.");
    }

    public CurrencyNotFoundException(String message) {
        super(message);
    }
}