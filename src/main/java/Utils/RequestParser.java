package Utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;

public class RequestParser {

    private static final String UTF_8_ENCODING = "UTF-8";

    public static String[] parseRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        String[] pairs = body.toString().split("&");
        String name = null;
        String sign = null;

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], UTF_8_ENCODING);
                String value = URLDecoder.decode(keyValue[1], UTF_8_ENCODING);
                if (key.equals("name")) {
                    name = value;
                }
                else if (key.equals("sign")) {
                    sign = value;
                }
            }
        }

        return new String[] {name, sign};
    }
}
