package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Response {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CHARACTER_ENCODING_UTF8 = "UTF-8";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendJsonResponse(HttpServletResponse response, Object responseData) throws IOException {

        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding(CHARACTER_ENCODING_UTF8);

        String jsonResponse = objectMapper.writeValueAsString(responseData);
        response.getWriter().write(jsonResponse);
    }

    public static void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) {
        try {
            response.setStatus(statusCode);
            ResponseError error = new ResponseError(statusCode, errorMessage);
            sendJsonResponse(response, error);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}