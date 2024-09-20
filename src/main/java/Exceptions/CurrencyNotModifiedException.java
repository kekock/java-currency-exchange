package Exceptions;

public class CurrencyNotModifiedException extends RuntimeException {
    public CurrencyNotModifiedException() {
        super("Currency not modified");
    }

    public CurrencyNotModifiedException(String message) {
        super(message);
    }
}
