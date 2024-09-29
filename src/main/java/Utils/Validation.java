package Utils;

public class Validation {

    private static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";
    private static final String NUMERIC_REGEX = "^[\\d]+([.,][\\d]+)?$";
    private static final String EXCHANGE_RATE_CODE_REGEX = "^[A-Z]{6}$";

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static String extractCurrencyCode(String pathInfo) {
        return pathInfo.substring(1);
    }

    public static boolean isValidCurrencyCode(String currencyCode) {
        return !isNullOrEmpty(currencyCode) && currencyCode.matches(CURRENCY_CODE_REGEX);
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
        return !isNullOrEmpty(exchangeRateCode) && exchangeRateCode.matches(EXCHANGE_RATE_CODE_REGEX);
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