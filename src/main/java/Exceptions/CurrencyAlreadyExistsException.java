package Exceptions;

public class CurrencyAlreadyExistsException extends RuntimeException {
    public CurrencyAlreadyExistsException() {
        super("Currency already exists.");
    }

    public CurrencyAlreadyExistsException(String message) {
        super(message);
    }
}