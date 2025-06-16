package controllers;

import api.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseController {
public static final ObjectMapper mapper = new ObjectMapper();

    public static void sendJsonResponse(Object data, Response res) {
        try {
            String jsonResponse = mapper.writeValueAsString(data);
            res.setBody(jsonResponse);
            res.send(HttpURLConnection.HTTP_OK);
        } catch(JsonProcessingException e) {
            sendErrorResponse(res, "Serialization error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            sendErrorResponse(res, "Unexpected", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void sendJsonResponseWithMessage(String msg, Object object, Response res) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("message", msg);
            data.put("data", object);

            String jsonResponse = mapper.writeValueAsString(data);
            res.setBody(jsonResponse);
            res.send(HttpURLConnection.HTTP_OK);
        } catch(JsonProcessingException e) {
            sendErrorResponse(res, "Serialization error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void sendErrorResponse(Response res, String type, String msg, int status) {

        try {
            Map<String, Object> resJsonMap = new LinkedHashMap<>();
            resJsonMap.put("type", type);
            resJsonMap.put("error", msg);

            String jsonResponse = mapper.writeValueAsString(resJsonMap);
            res.setBody(jsonResponse);
            res.send(status);
        } catch (JsonProcessingException e) {
            sendErrorResponse(res, "Serialization error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
