package controllers;

import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseController {
public static final ObjectMapper mapper = new ObjectMapper();

    public static void sendJsonResponse(Object data, Response res) {
        try {
            String jsonResponse = mapper.writeValueAsString(data);
            res.setBody(jsonResponse);
            res.send(HttpURLConnection.HTTP_OK);
        } catch(Exception e) {
            sendErrorResponse(res, "Serialization error: ");
        }

    }

    public static void sendErrorResponse(Response res, String message) {
        Map<String, Object> resJsonMap = new HashMap<>();
        resJsonMap.put("ERROR", message);

        try {
            String jsonResponse = mapper.writeValueAsString(resJsonMap);
            res.setBody(jsonResponse);
            res.send(HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            System.out.println("Serialization error: " + e.getMessage());
        }
    }
}
