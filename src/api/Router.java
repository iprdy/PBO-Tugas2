package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.ResponseController;
import controllers.RouterController;
import controllers.VillasController;
import models.Villas;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void handleGetRequest(String path, Response res) {
        try {
            if (path.equals("/villas")) {
                RouterController.handleGetAllVilla(res);
            }

            else if (path.matches("/villas/\\d+$")) {
                RouterController.handleGetVillaById(path, res);
            }

            else if (path.matches("/villas/\\d+/rooms$")) {
                RouterController.handleGetVillaIdRooms(path, res);
            }

            else if (path.matches("/villas/\\d+/bookings$")) {
                RouterController.handleGetVillaIdBookings(path, res);
            }

            else if (path.matches("/villas/\\d+/reviews")) {
                RouterController.handleGetVillaIdReviews(path, res);
            }

            else if (path.matches("/villas?ci_date=\\d{4}-\\d{2}-\\d{2}&co_date=\\d{4}-\\d{2}-\\d{2}")) {

            }

            else if (path.equals("/customers")) {
                RouterController.handleGetAllCustomer(res);
            }

            else if (path.matches("/customers/\\d+")) {
                RouterController.handleGetCustomerById(path, res);
            }

            else if (path.matches("/customers/\\d+/bookings")) {
                RouterController.handleGetCustomerIdBookings(path, res);
            }

            else if (path.matches("/customers/\\d+/reviews")) {
                RouterController.handleGetCustomerIdReviews(path, res);
            }

            else if (path.equals("/vouchers")) {

            }

            else if (path.matches("/vouchers/\\d+")) {

            }
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }
}
