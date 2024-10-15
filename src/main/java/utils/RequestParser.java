package utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;

public class RequestParser {

    private static final String UTF_8_ENCODING = "UTF-8";

    public static String[] parseNameAndSignFromBody(HttpServletRequest request) throws IOException {
        return parseParameters(request, new String[]{"name", "sign"});
    }

    public static String parseRateFromBody(HttpServletRequest request) throws IOException {
        String[] result = parseParameters(request, new String[]{"rate"});
        return result.length > 0 ? result[0] : null;
    }

    private static String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        return body.toString();
    }

    private static String[] parseParameters(HttpServletRequest request, String[] keys) throws IOException {
        String body = getRequestBody(request);
        String[] pairs = body.split("&");
        String[] values = new String[keys.length];

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], UTF_8_ENCODING);
                String value = URLDecoder.decode(keyValue[1], UTF_8_ENCODING);
                for (int i = 0; i < keys.length; i++) {
                    if (key.equals(keys[i])) {
                        values[i] = value;
                    }
                }
            }
        }
        return values;
    }
}