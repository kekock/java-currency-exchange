package Utils;

import java.util.Currency;

public class Validation {
    private static final String NUMERIC_REGEX = "^[\\d]+([.,][\\d]+)?$";

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
        return !isNullOrEmpty(exchangeRateCode)
                && isValidCurrencyCode(exchangeRateCode.substring(0,3))
                && isValidCurrencyCode(exchangeRateCode.substring(3));
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
        return !isNullOrEmpty(rate) && rate.matches(NUMERIC_REGEX);
    }
}