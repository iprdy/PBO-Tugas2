package api;

import controllers.ResponseController;
import controllers.RouterController;

import java.net.HttpURLConnection;

public class Router {
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

//            else if (path.matches("/villas?ci_date=\\d{4}-\\d{2}-\\d{2}&co_date=\\d{4}-\\d{2}-\\d{2}")) {
//
//            }

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
                RouterController.handleGetAllVouchers(res);
            }

            else if (path.matches("/vouchers/\\d+")) {
                RouterController.handleGetVoucherById(path, res);
            }
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePostRequest(String path, Response res, Request req) {
        try {
            if (path.equals("/villas")) {
                RouterController.handlePostVilla(res,req);
            }

            else if (path.matches("/villas/\\d+/rooms$")) {
                RouterController.handlePostVillaIdRooms(path, res, req);
            }

            else if (path.matches("/customers")) {
                RouterController.handlePostCustomer(res);
            }

            else if (path.matches("/customers/\\d+/bookings")) {
                RouterController.handlePostCustomerIdBookings(path, res);
            }

            else if (path.matches("/customers/\\d+/bookings/\\d+/reviews")) {
                RouterController.handlePostCustomerIdBookingsIdReviews(path, res);
            }

            else if (path.matches("/vouchers")) {
                RouterController.handlePostVouchers(res);
            }
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }
}
