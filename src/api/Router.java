package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.VillasController;
import models.Villas;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void handleGetRequest(String path, Response res) {
        if (path.equals("/villas")) {

        }

        else if (path.matches("/villas/\\d+$")) {

        }

        else if (path.matches("/villas/\\d+/rooms$")) {

        }

        else if (path.matches("/villas/\\d+/bookings$")) {

        }

        else if (path.matches("/villas/\\d+/reviews")) {

        }

        else if (path.matches("/villas?ci_date=\\d+&co_date=\\d+")) {

        }

        else if (path.equals("/customers")) {

        }

        else if (path.matches("/customers/\\d+")) {

        }

        else if (path.matches("/customers/\\d+/bookings")) {

        }

        else if (path.matches("/customers/\\d+/reviews")) {

        }

        else if (path.equals("/vouchers")) {

        }

        else if (path.matches("/vouchers/\\d+")) {

        }
    }

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
