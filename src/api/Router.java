package api;

import controllers.RouterController;
import exceptions.BadRequestException;
import exceptions.ExceptionHandler;

public class Router {
    public static void handleGetRequest(String path, Response res, Request req) {
        try {
            if (path.equals("/villas")) {
                RouterController.handleGetSlashVillas(res, req);
            }

            else if (path.matches("/villas/\\d+$")) {
                RouterController.handleGetVillaById(path, res);
            }

            else if (path.matches("/villas/\\d+/rooms$")) {
                RouterController.handleGetRoomsByVillaId(path, res);
            }

            else if (path.matches("/villas/\\d+/bookings$")) {
                RouterController.handleGetBookingsByVillaId(path, res);
            }

            else if (path.matches("/villas/\\d+/reviews")) {
                RouterController.handleGetReviewsByVillaId(path, res);
            }

            else if (path.equals("/customers")) {
                RouterController.handleGetAllCustomers(res);
            }

            else if (path.matches("/customers/\\d+")) {
                RouterController.handleGetCustomerById(path, res);
            }

            else if (path.matches("/customers/\\d+/bookings")) {
                RouterController.handleGetCustomerBookingsById(path, res);
            }

            else if (path.matches("/customers/\\d+/reviews")) {
                RouterController.handleGetCustomerReviewsById(path, res);
            }

            else if (path.equals("/vouchers")) {
                RouterController.handleGetAllVouchers(res);
            }

            else if (path.matches("/vouchers/\\d+")) {
                RouterController.handleGetVoucherById(path, res);
            }

            else {
                throw new BadRequestException("Path '" + path + "' bukan path yang valid");
            }
        } catch (Exception e) {
            ExceptionHandler.handleException(e, res);
        }
    }

    public static void handlePostRequest(String path, Response res, Request req) {
        try {
            if (path.equals("/villas")) {
                RouterController.handlePostVilla(res,req);
            }

            else if (path.matches("/villas/\\d+/rooms$")) {
                RouterController.handlePostRoomTypeByVillaId(path, res, req);
            }

            else if (path.matches("/customers")) {
                RouterController.handlePostCustomer(res, req);
            }

            else if (path.matches("/customers/\\d+/bookings")) {
                RouterController.handlePostBookingByCustomerId(path, res, req);
            }

            else if (path.matches("/customers/\\d+/bookings/\\d+/reviews")) {
                RouterController.handlePostReviewByCustomerAndBookingId(path, res, req);
            }

            else if (path.matches("/vouchers")) {
                RouterController.handlePostVoucher(res, req);
            }

            else {
                throw new BadRequestException("Path '" + path + "' bukan path yang valid");
            }

        } catch (Exception e) {
            ExceptionHandler.handleException(e, res);
        }
    }

    public static void handlePutRequest(String path, Response res, Request req) {
        try {
            if (path.matches("/villas/\\d+$")) {
                RouterController.handlePutVillaById(path, res, req);
            }

            else if (path.matches("/villas/\\d+/rooms/\\d+$")) {
                RouterController.handlePutRoomTypeByVillaId(path, res, req);
            }

            else if (path.matches("/customers/\\d+$")) {
                RouterController.handlePutCustomerById(path, res, req);
            }

            else if (path.matches("/vouchers/\\d+$")) {
                RouterController.handlePutVoucherById(path, res, req);
            }

            else {
                throw new BadRequestException("Path '" + path + "' bukan path yang valid");
            }
        } catch (Exception e) {
            ExceptionHandler.handleException(e, res);        }
    }

    public static void handleDeleteRequest(String path, Response res) {
        try {
            if (path.matches("/villas/\\d+/rooms/\\d+$")) {
                RouterController.handleDeleteRoomTypeByVillaId(path, res);
            }

            else if (path.matches("/villas/\\d+$")) {
                RouterController.handleDeleteVillaById(path, res);
            }

            else if (path.matches("/vouchers/\\d+$")) {
                RouterController.handleDeleteVoucherById(path, res);
            }

            else {
                throw new BadRequestException("Path '" + path + "' bukan path yang valid");
            }
        } catch (Exception e) {
            ExceptionHandler.handleException(e, res);        }
    }
}
