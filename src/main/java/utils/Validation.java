package utils;

import java.util.Currency;
import java.util.regex.Pattern;

public class Validation {
    private static final Pattern NUMERIC_REGEX = Pattern.compile("^[\\d]+([.,][\\d]+)?$");

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static String extractCurrencyCode(String pathInfo) {
        return pathInfo.substring(1);
    }

    public static boolean isValidCurrencyCode(String currencyCode) {
        if (isNullOrEmpty(currencyCode)) {
            return false;
        }
        try {
            Currency currency = Currency.getInstance(currencyCode);
            return currency.getCurrencyCode().equals(currencyCode);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidCurrencyPath(String pathInfo) {
        return !isNullOrEmpty(pathInfo) && pathInfo.length() > 1 &&
                isValidCurrencyCode(extractCurrencyCode(pathInfo));
    }

    public static boolean areValidCurrencyFormFields(String currencyName, String currencyCode, String currencySign) {
        return !isNullOrEmpty(currencyName) && !isNullOrEmpty(currencySign)
                && isValidCurrencyCode(currencyCode);
    }

    public static String extractPairCode(String pathInfo) {
        return pathInfo.substring(1);
    }

    public static boolean isValidPairCode(String exchangeRateCode) {
        if (isNullOrEmpty(exchangeRateCode)) {
            return false;
        }
        String baseCurrency = exchangeRateCode.substring(0, 3);
        String targetCurrency = exchangeRateCode.substring(3);

        return isValidCurrencyCode(baseCurrency) && isValidCurrencyCode(targetCurrency);
    }

    public static boolean isValidPairPath(String pathInfo) {
        return !isNullOrEmpty(pathInfo) && pathInfo.length() > 1 &&
                isValidPairCode(extractPairCode(pathInfo));
    }

    public static boolean areValidExchangeRateFormFields(String baseCurrencyCode, String targetCurrencyCode, String exchangeRate) {
        return isValidCurrencyCode(baseCurrencyCode) &&
                isValidCurrencyCode(targetCurrencyCode) &&
                isValidRate(exchangeRate);
    }

    private static boolean isValidRate(String rate) {
        return !isNullOrEmpty(rate) && NUMERIC_REGEX.matcher(rate).matches();
    }
}