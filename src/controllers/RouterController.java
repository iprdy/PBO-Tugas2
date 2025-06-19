package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.List;

public class RouterController {
    public static ObjectMapper mapper = new ObjectMapper();
    //GET
    public static void handleGetAllVilla(Response res) {
        try {
            VillasController vc = new VillasController();
            List<Villas> villa = vc.getAllVillas();

            ResponseController.sendJsonResponse(villa, res);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetVillaById(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            VillasController vc = new VillasController();
            Villas villa = vc.getVillaById(id);

            ResponseController.sendJsonResponse(villa, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }

    }

    public static void handleGetVillaIdRooms(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            VillasController vc = new VillasController();
            List<RoomTypes> rt = vc.getRoomsByVillaId(id);

            ResponseController.sendJsonResponse(rt, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }

    }

    public static void handleGetVillaIdBookings(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            VillasController vc = new VillasController();
            List<Booking> bookings = vc.getBookingsByVillaId(id);

            ResponseController.sendJsonResponse(bookings, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetVillaIdReviews(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            ReviewController rc = new ReviewController();
            List<Review> reviews = rc.getReviewsByVillaId(id);

            ResponseController.sendJsonResponse(reviews, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

//    public static void handleGetVillaCinCout(String path, Response res) {}

    public static void handleGetAllCustomer(Response res) {
        try {
            CustomerController cc = new CustomerController();
            List<Customer> customers = cc.getAllCustomers();

            ResponseController.sendJsonResponse(customers, res);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetCustomerById(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController cc = new CustomerController();
            Customer customer = cc.getCustomerById(id);

            ResponseController.sendJsonResponse(customer, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetCustomerIdBookings(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController cc = new CustomerController();
            List<Booking> bookings = cc.getCustomerBookings(id);

            ResponseController.sendJsonResponse(bookings, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetCustomerIdReviews(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            ReviewController rc = new ReviewController();
            List<Review> reviews = rc.getReviewsByCustomerId(id);

            ResponseController.sendJsonResponse(reviews, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetAllVouchers(Response res) {
        try {
            VoucherController vc = new VoucherController();
            List<Voucher> vouchers = vc.getAllVouchers();

            ResponseController.sendJsonResponse(vouchers, res);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetVoucherById(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            VoucherController vc = new VoucherController();
            Voucher voucher = vc.getVoucherById(id);

            if (voucher != null) {
                ResponseController.sendJsonResponse(voucher, res);
            } else {
                ResponseController.sendErrorResponse(res, "Voucher tidak ditemukan", "ID: " + id, HttpURLConnection.HTTP_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }




    //POST
    public static void handlePostVilla(Response res, Request req) {
        try {
            VillasController vc = new VillasController();
            String body = req.getBody();
            Villas villa = mapper.readValue(body, Villas.class);
            vc.createVilla(villa);

            ResponseController.sendJsonResponseWithMessage("Berhasil membuat villa", villa, res);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePostVillaIdRooms(String path, Response res, Request req) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            VillasController vc = new VillasController();
            String body = req.getBody();
            RoomTypes rt = mapper.readValue(body, RoomTypes.class);
            rt.setVilla_id(id);
            vc.createVillasRooms(rt);

            ResponseController.sendJsonResponseWithMessage("Berhasil membuat room type di villa dengan id " + id, rt, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePostCustomer(Response res, Request req) {
        try {
            CustomerController cc = new CustomerController();
            String body = req.getBody();
            Customer customer = mapper.readValue(body, Customer.class);
            cc.postCustomer(customer);

            ResponseController.sendJsonResponseWithMessage("Berhasil membuat customer", customer, res);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePostCustomerIdBookings(String path, Response res, Request req) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController cc = new CustomerController();
            String body = req.getBody();
            Booking booking = mapper.readValue(body, Booking.class);
            booking.setCustomer(id);
            cc.postBookingForCustomer(booking, id);

            ResponseController.sendJsonResponseWithMessage("Berhasil membuat booking di costumer dengan id " + id, booking, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePostCustomerIdBookingsIdReviews(String path, Response res, Request req) {
        try {
            int cid = Integer.parseInt(path.split("/")[2]);
            int bid = Integer.parseInt(path.split("/")[4]);
            ReviewController rc = new ReviewController();
            String body = req.getBody();
            Review review = mapper.readValue(body, Review.class);
            rc.postReviewForBooking(review, cid, bid);

            ResponseController.sendJsonResponseWithMessage("Berhasil membuat review di costumer dengan id " + cid + "dan booking dengan id " + bid, review, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePostVouchers(Response res, Request req) {
        try {
            String body = req.getBody();
            Voucher voucher = mapper.readValue(body, Voucher.class);

            VoucherController vc = new VoucherController();
            vc.postVoucher(voucher);

            ResponseController.sendJsonResponseWithMessage("Berhasil membuat voucher", voucher, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }




    //PUT
    public static void handlePutVillaById(String path, Response res, Request req) throws SQLException {
        try {
            VillasController vc = new VillasController();
            int id = Integer.parseInt(path.split("/")[2]);
            String body = req.getBody();
            Villas villa = mapper.readValue(body, Villas.class);
            villa.setId(id);
            vc.updateVilla(villa);

            ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate villa dengan id" + id, villa, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }

    }

    public static void handlePutVillaIdRoomsId(String path, Response res, Request req) throws SQLException {
        try {
            VillasController vc = new VillasController();
            int vid = Integer.parseInt(path.split("/")[2]);
            int rid = Integer.parseInt(path.split("/")[4]);
            String body = req.getBody();
            RoomTypes rt = mapper.readValue(body, RoomTypes.class);
            rt.setIdAndVillaId(rid, vid);
            vc.updateVillasRoomTypes(rt);

            ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate roomtype dengan id " + rid + " di villa dengan id " + vid, rt, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePutCustomerById(String path, Response res, Request req) throws SQLException {
        try {
            CustomerController cc = new CustomerController();
            int id = Integer.parseInt(path.split("/")[2]);
            String body = req.getBody();
            Customer customer = mapper.readValue(body, Customer.class);
            customer.setId(id);
            cc.updateCustomer(customer);

            ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate customer dengan id " + id, customer, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handlePutVoucherById(String path, Response res, Request req) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            String body = req.getBody();
            Voucher voucher = mapper.readValue(body, Voucher.class);
            voucher.setId(id);

            VoucherController vc = new VoucherController();
            vc.updateVoucher(voucher);

            ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate voucher dengan id " + id, voucher, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonMappingException e) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (JsonProcessingException e) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }





    //DELETE
    public static void handleDeleteVillaIdRoomsId(String path, Response res) throws SQLException {
        try {
            VillasController vc = new VillasController();
            int vid = Integer.parseInt(path.split("/")[2]);
            int rid = Integer.parseInt(path.split("/")[4]);
            vc.deleteVillaRoomTypes(rid, vid);

            ResponseController.sendJsonResponseWithMessage("Berhasil menghapus roomtype dengan id " + rid + " di villa dengan id " + vid, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleDeleteVillaById(String path, Response res) throws SQLException {
        try {
            VillasController vc = new VillasController();
            int id = Integer.parseInt(path.split("/")[2]);
            vc.deleteVilla(id);

            ResponseController.sendJsonResponseWithMessage("Berhasil menghapus villa dengan id " + id, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleDeleteVoucherById(String path, Response res) {
        try {
            int id = Integer.parseInt(path.split("/")[2]);
            VoucherController vc = new VoucherController();
            vc.deleteVoucher(id);

            ResponseController.sendJsonResponseWithMessage("Berhasil menghapus voucher dengan id " + id, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }
}
