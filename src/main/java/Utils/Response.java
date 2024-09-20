package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Response {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CHARACTER_ENCODING_UTF8 = "UTF-8";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeJsonResponse(HttpServletResponse response, Object data) throws IOException {

        String jsonResponse = objectMapper.writeValueAsString(data);

        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding(CHARACTER_ENCODING_UTF8);
        response.getWriter().write(jsonResponse);
    }
}
