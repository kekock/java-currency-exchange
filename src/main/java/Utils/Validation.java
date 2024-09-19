package Utils;

public class Validation {

    private static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";

    public static String extractCurrencyCode(String pathInfo) {
        return pathInfo.substring(1);
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean isValidCurrencyCode(String currencyCode) {
        return !isNullOrEmpty(currencyCode) && currencyCode.matches(CURRENCY_CODE_REGEX);
    }

    public static boolean isValidCurrencyPath (String pathInfo) {
        if (!isNullOrEmpty(pathInfo) && pathInfo.length() > 1) {
            String currencyCode = extractCurrencyCode(pathInfo);
            return isValidCurrencyCode(currencyCode);
        }

        return false;
    }

    public static boolean areValidFormFields(String currencyName, String currencyCode, String currencySign){
        return !isNullOrEmpty(currencyName) && !isNullOrEmpty(currencyCode)
                && !isNullOrEmpty(currencySign) && currencyCode.matches(CURRENCY_CODE_REGEX);
    }
}
