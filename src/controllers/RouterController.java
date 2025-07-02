package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import util.GlobalValidator;
import util.VillaValidator;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// Static final untuk koneksi database
final class DBConfig {
    public static final String DB_URL = "jdbc:sqlite:villa_booking.db";
}

public class RouterController {
    public static ObjectMapper mapper = new ObjectMapper();

    public static int extractIdFromPath(String path, int positionFromEnd) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - positionFromEnd]);
    }

    //GET
    public static void handleGetSlashVillas(Response res, Request req) throws Exception {
        Map<String, String> query = VillaValidator.validateQuery(req);

        if (!query.isEmpty()) {
            String ciDate = query.get("ci_date");
            String coDate = query.get("co_date");

            handleGetAvaibleVillas(ciDate, coDate, res);

        } else {
            handleGetAllVilla(res);
        }
    }

    public static void handleGetAllVilla(Response res) throws Exception {
        VillasController vc = new VillasController();
        List<Villas> villa = vc.getAllVillas();

        ResponseController.sendJsonResponse(villa, res);
    }

    public static void handleGetVillaById(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        VillasController vc = new VillasController();
        Villas villa = GlobalValidator.dataRequireNonNull(vc.getVillaById(id), "Villa dengan id " + id + " tidak ditemukan");

        ResponseController.sendJsonResponse(villa, res);
    }

    public static void handleGetVillaIdRooms(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        VillasController vc = new VillasController();
        List<RoomTypes> rt = vc.getRoomsByVillaId(id);

        ResponseController.sendJsonResponse(rt, res);
    }

    public static void handleGetVillaIdBookings(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        VillasController vc = new VillasController();
        List<Booking> bookings = vc.getBookingsByVillaId(id);

        ResponseController.sendJsonResponse(bookings, res);
    }

    public static void handleGetVillaIdReviews(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        ReviewController rc = new ReviewController();
        List<Review> reviews = rc.getReviewsByVillaId(id);

        ResponseController.sendJsonResponse(reviews, res);
    }

    public static void handleGetAvaibleVillas(String checkIn, String checkOut, Response res) throws Exception {
        VillasController vc = new VillasController();
        List<Villas> villas = vc.searchAvailableVillas(checkIn, checkOut);
        GlobalValidator.dataRequireNonNull(villas, "Tidak ada villa yang tersedia");

        ResponseController.sendJsonResponse(villas, res);
    }

    public static void handleGetAllCustomer(Response res) throws Exception {
        CustomerController cc = new CustomerController();
        List<Customer> customers = cc.getAllCustomers();

        ResponseController.sendJsonResponse(customers, res);
    }

    public static void handleGetCustomerById(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        CustomerController cc = new CustomerController();
        Customer customer = cc.getCustomerById(id);

        ResponseController.sendJsonResponse(customer, res);
    }

    public static void handleGetCustomerIdBookings(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        CustomerController cc = new CustomerController();
        List<Booking> bookings = cc.getCustomerBookings(id);

        ResponseController.sendJsonResponse(bookings, res);
    }

    public static void handleGetCustomerIdReviews(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        ReviewController rc = new ReviewController();
        List<Review> reviews = rc.getReviewsByCustomerId(id);

        ResponseController.sendJsonResponse(reviews, res);
    }

    public static void handleGetAllVouchers(Response res) throws Exception {
        VoucherController vc = new VoucherController();
        List<Voucher> vouchers = vc.getAllVouchers();

        ResponseController.sendJsonResponse(vouchers, res);
    }

    public static void handleGetVoucherById(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        VoucherController vc = new VoucherController();
        Voucher voucher = vc.getVoucherById(id);

        if (voucher != null) {
                ResponseController.sendJsonResponse(voucher, res);
        } else {
            ResponseController.sendErrorResponse(res, "Voucher tidak ditemukan", "ID: " + id, HttpURLConnection.HTTP_NOT_FOUND);
        }
    }




    //POST
    public static void handlePostVilla(Response res, Request req) throws Exception {
        VillasController vc = new VillasController();
        String body = req.getBody();
        Villas villa = mapper.readValue(body, Villas.class);
        VillaValidator.validatePostVilla(villa);
        vc.createVilla(villa);

        ResponseController.sendJsonResponseWithMessage("Berhasil membuat villa", villa, res);
    }


    public static void handlePostVillaIdRooms(String path, Response res, Request req) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        VillasController vc = new VillasController();
        GlobalValidator.dataRequireNonNull(vc.getVillaById(id), "Villa dengan id " + id + " tidak ditemukan");
        String body = req.getBody();
        RoomTypes rt = mapper.readValue(body, RoomTypes.class);
        VillaValidator.validatePostVillaRooms(rt);
        rt.setVilla_id(id);
        vc.createVillasRooms(rt);

        ResponseController.sendJsonResponseWithMessage("Berhasil membuat room type di villa dengan id " + id, rt, res);
    }

    public static void handlePostCustomer(Response res, Request req) throws Exception{
        CustomerController cc = new CustomerController();
        String body = req.getBody();
        Customer customer = mapper.readValue(body, Customer.class);
        cc.postCustomer(customer);

        ResponseController.sendJsonResponseWithMessage("Berhasil membuat customer", customer, res);
    }

    public static void handlePostCustomerIdBookings(String path, Response res, Request req) throws Exception {
        int id = extractIdFromPath(path, 2);

        CustomerController cc = new CustomerController();
        String body = req.getBody();
        Booking booking = mapper.readValue(body, Booking.class);
        booking.setCustomer(id);
        cc.postBookingForCustomer(booking, id);

        ResponseController.sendJsonResponseWithMessage("Berhasil membuat booking di costumer dengan id " + id, booking, res);
    }

    public static void handlePostCustomerIdBookingsIdReviews(String path, Response res, Request req) throws Exception {
        int bid = extractIdFromPath(path, 2); // paling akhir
        int cid = extractIdFromPath(path, 4); // 2 sebelum itu

        ReviewController rc = new ReviewController();
        String body = req.getBody();
        Review review = mapper.readValue(body, Review.class);
        rc.postReviewForBooking(review, cid, bid);

        ResponseController.sendJsonResponseWithMessage("Berhasil membuat review di costumer dengan id " + cid + "dan booking dengan id " + bid, review, res);
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
    public static void handlePutVillaById(String path, Response res, Request req) throws Exception {
        VillasController vc = new VillasController();
        int id = extractIdFromPath(path, 1);
        Villas oldVilla = GlobalValidator.dataRequireNonNull(vc.getVillaById(id), "Villa dengan id " + id + " tidak ditemukan");
        String body = req.getBody();
        Villas newVilla = mapper.readValue(body, Villas.class);
        VillaValidator.validatePostVilla(newVilla);
        newVilla.setId(id);
        vc.updateVilla(newVilla);

        ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate villa dengan id " + id, oldVilla, newVilla, res);
    }

    public static void handlePutVillaIdRoomsId(String path, Response res, Request req) throws Exception {
        VillasController vc = new VillasController();
        int rid = extractIdFromPath(path, 1); // paling akhir
        int vid = extractIdFromPath(path, 3); // 2 sebelum itu

        String body = req.getBody();
        RoomTypes rt = mapper.readValue(body, RoomTypes.class);
        rt.setIdAndVillaId(rid, vid);
        vc.updateVillasRoomTypes(rt);

        ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate roomtype dengan id " + rid + " di villa dengan id " + vid, rt, res);
    }

    public static void handlePutCustomerById(String path, Response res, Request req) throws Exception {
        CustomerController cc = new CustomerController();
        int id = extractIdFromPath(path, 1);

        String body = req.getBody();
        Customer customer = mapper.readValue(body, Customer.class);
        customer.setId(id);
        cc.updateCustomer(customer);

        ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate customer dengan id " + id, customer, res);
    }

    public static void handlePutVoucherById(String path, Response res, Request req) {
        try {
            int id = extractIdFromPath(path, 1);

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
    public static void handleDeleteVillaIdRoomsId(String path, Response res) throws Exception {
        VillasController vc = new VillasController();
        int rid = extractIdFromPath(path, 1); // paling akhir
        int vid = extractIdFromPath(path, 3); // 2 sebelum itu
        VillaValidator.checkVillaIdAndRoomTypeId(rid, vid);

        vc.deleteVillaRoomTypes(rid, vid);

        ResponseController.sendJsonResponseWithMessage("Berhasil menghapus roomtype dengan id " + rid + " di villa dengan id " + vid, res);
    }

    public static void handleDeleteVillaById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);

        VillasController vc = new VillasController();
        GlobalValidator.dataRequireNonNull(vc.getVillaById(id), "Villa dengan id " + id + " tidak ditemukan");

        vc.deleteVilla(id);

        ResponseController.sendJsonResponseWithMessage("Berhasil menghapus villa dengan id " + id, res);
    }

    public static void handleDeleteVoucherById(String path, Response res) {
        try {
            int id = extractIdFromPath(path, 1);
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
