package pl.kurs.shapeapiapp.utils;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static byte[] convertObjectToJsonBytes(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON: " + e.getMessage(), e);
        }
    }

    public static String getTokenFromJson(String json) {
        return json.substring(13).replaceAll("\\\"}", "");
    }
}
