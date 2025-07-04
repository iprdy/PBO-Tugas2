package controllers;

import api.Request;
import api.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import util.*;

import java.util.List;
import java.util.Map;

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
            //GET Available Villas
            List<Villas> availableVillas = GlobalValidator.dataRequireNonNull(
                    new VillasController().searchAvailableVillas(query.get("ci_date"), query.get("co_date")),
                    "Tidak ada villa yang tersedia"
            );

            ResponseController.sendJsonResponse(availableVillas, res);
        } else {
            //GET All Villas
            List<Villas> villas = GlobalValidator.dataRequireNonNull(
                    new VillasController().getAllVillas(),
                    "Tidak ada data villa yang tersedia"
            );

            ResponseController.sendJsonResponse(villas, res);
        }
    }

    public static void handleGetVillaById(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);

        Villas villa = GlobalValidator.dataRequireNonNull(
                new VillasController().getVillaById(id),
                "Villa dengan id " + id + " tidak ditemukan"
        );

        ResponseController.sendJsonResponse(villa, res);
    }

    public static void handleGetVillaIdRooms(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        List<RoomTypes> rt = new VillasController().getRoomsByVillaId(id);

        ResponseController.sendJsonResponse(rt, res);
    }

    public static void handleGetVillaIdBookings(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        List<Booking> bookings = new VillasController().getBookingsByVillaId(id);

        ResponseController.sendJsonResponse(bookings, res);
    }

    public static void handleGetVillaIdReviews(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);

        List<Review> reviews = GlobalValidator.dataRequireNonNull(
                new ReviewController().getReviewsByVillaId(id),
                "Villa dengan id " + id + " tidak ditemukan"
        );

        ResponseController.sendJsonResponse(reviews, res);
    }

    public static void handleGetAllCustomer(Response res) throws Exception {
        List<Customer> customers = new CustomerController().getAllCustomers();

        ResponseController.sendJsonResponse(customers, res);
    }

    public static void handleGetCustomerById(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);

        Customer customer = GlobalValidator.dataRequireNonNull(
                new CustomerController().getCustomerById(id),
                "Villa dengan id " + id + " tidak ditemukan"
        );

        ResponseController.sendJsonResponse(customer, res);
    }

    public static void handleGetCustomerIdBookings(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);
        List<Booking> bookings = new CustomerController().getCustomerBookings(id);

        ResponseController.sendJsonResponse(bookings, res);
    }

    public static void handleGetCustomerIdReviews(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);

        List<Review> reviews = GlobalValidator.dataRequireNonNull(
                new ReviewController().getReviewsByCustomerId(id),
                "Belum ada review untuk customer dengan id " + id);

        ResponseController.sendJsonResponse(reviews, res);
    }

    public static void handleGetAllVouchers(Response res) throws Exception {
        List<Voucher> vouchers = new VoucherController().getAllVouchers();

        ResponseController.sendJsonResponse(vouchers, res);
    }

    public static void handleGetVoucherById(String path, Response res) throws Exception {
        int id = Integer.parseInt(path.split("/")[2]);

        Voucher voucher = GlobalValidator.dataRequireNonNull(
                new VoucherController().getVoucherById(id),
                "Voucher dengan id " + id + " tidak ditemukan"
        );

        ResponseController.sendJsonResponse(voucher, res);
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
        CustomerValidator.validatePostCustomer(customer);
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
        int bookingId = extractIdFromPath(path, 2);  // /.../bookings/1/reviews
        int customerId = extractIdFromPath(path, 4); // /customers/1/...

        // Validasi pemesanan memang milik customer
        ReviewValidator.checkBookingBelongsToCustomer(bookingId, customerId);

        // Ambil data review dari body
        String body = req.getBody();
        Review review = mapper.readValue(body, Review.class);

        // Validasi isi review
        ReviewValidator.validatePostReview(review);

        // Set booking ID dari path (bukan dari body)
        review.setBooking(bookingId);

        // Masukkan ke database
        ReviewController rc = new ReviewController();
        rc.postReviewForBooking(review, customerId, bookingId);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil membuat review untuk customer dengan id " + customerId + " dan booking dengan id " + bookingId,
                review,
                res
        );
    }

    public static void handlePostVouchers(Response res, Request req) throws Exception {
        String body = req.getBody();
        Voucher voucher = mapper.readValue(body, Voucher.class);

        VoucherController vc = new VoucherController();
        VoucherValidator.validatePostVoucher(voucher);
        vc.postVoucher(voucher);

        ResponseController.sendJsonResponseWithMessage("Berhasil membuat voucher", voucher, res);
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
        int rid = extractIdFromPath(path, 1); // paling akhir
        int vid = extractIdFromPath(path, 3); // 2 sebelum itu
        VillasController vc = new VillasController();
        RoomTypes oldRoomType = GlobalValidator.dataRequireNonNull(vc.getVillaRoomById(rid,vid), "ID Villa atau Room Type tidak ditemukan");

        String body = req.getBody();
        RoomTypes newRoomType = mapper.readValue(body, RoomTypes.class);
        newRoomType.setIdAndVillaId(rid, vid);
        vc.updateVillasRoomTypes(newRoomType);

        ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate roomtype dengan id " + rid + " di villa dengan id " + vid, oldRoomType, newRoomType, res);
    }

    public static void handlePutCustomerById(String path, Response res, Request req) throws Exception {
        CustomerController cc = new CustomerController();
        int id = extractIdFromPath(path, 1);

        Customer oldCustomer = GlobalValidator.dataRequireNonNull(cc.getCustomerById(id),"Customer dengan id " + id + " tidak ditemukan");

        String body = req.getBody();
        Customer newCustomer = mapper.readValue(body, Customer.class);
        newCustomer.setId(id);
        cc.updateCustomer(newCustomer);

        ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate customer dengan id " + id, oldCustomer, newCustomer, res);
    }

    public static void handlePutVoucherById(String path, Response res, Request req) throws Exception {
        int id = extractIdFromPath(path, 1);

        VoucherController vc = new VoucherController();
        Voucher oldVoucher = GlobalValidator.dataRequireNonNull(vc.getVoucherById(id), "Voucher dengan id " + id + " tidak ditemukan");

        String body = req.getBody();
        Voucher newVoucher = mapper.readValue(body, Voucher.class);
        newVoucher.setId(id);
        vc.updateVoucher(newVoucher);

        ResponseController.sendJsonResponseWithMessage("Berhasil mengupdate voucher dengan id " + id, oldVoucher, newVoucher, res);
    }




    //DELETE
    public static void handleDeleteVillaIdRoomsId(String path, Response res) throws Exception {
        VillasController vc = new VillasController();
        int rid = extractIdFromPath(path, 1); // paling akhir
        int vid = extractIdFromPath(path, 3); // 2 sebelum itu
        RoomTypes oldRoomType = GlobalValidator.dataRequireNonNull(vc.getVillaRoomById(rid, vid), "ID Villa atau Room Type tidak ditemukan");

        vc.deleteVillaRoomTypes(rid, vid);

        ResponseController.sendJsonResponseWithMessage("Berhasil menghapus roomtype dengan id " + rid + " di villa dengan id " + vid, oldRoomType, res);
    }

    public static void handleDeleteVillaById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);

        VillasController vc = new VillasController();
        Villas oldVilla = GlobalValidator.dataRequireNonNull(vc.getVillaById(id), "Villa dengan id " + id + " tidak ditemukan");

        vc.deleteVilla(id);

        ResponseController.sendJsonResponseWithMessage("Berhasil menghapus villa dengan id " + id, oldVilla, res);
    }

    public static void handleDeleteVoucherById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);

        VoucherController vc = new VoucherController();
        Voucher oldVoucher = GlobalValidator.dataRequireNonNull(vc.getVoucherById(id),"Voucher dengan id " + id + " tidak ditemukan");

        vc.deleteVoucher(id);

        ResponseController.sendJsonResponseWithMessage("Berhasil menghapus voucher dengan id " + id, oldVoucher, res);
    }
}