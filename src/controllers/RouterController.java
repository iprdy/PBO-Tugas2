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
        VillasController vc = new VillasController();

        if (!query.isEmpty()) {
            //GET Available Villas
            List<Villas> availableVillas = GlobalValidator.listRequireNotEmpty(
                    vc.searchAvailableVillas(query.get("ci_date"), query.get("co_date")),
                    "Tidak ada villa yang tersedia"
            );

            ResponseController.sendJsonResponse(availableVillas, res);
        } else {
            //GET All Villas
            List<Villas> villas = GlobalValidator.listRequireNotEmpty(
                    vc.getAllVillas(),
                    "Tidak ada data villa yang tersedia"
            );

            ResponseController.sendJsonResponse(villas, res);
        }
    }

    public static void handleGetVillaById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        VillasController vc = new VillasController();

        Villas villa = GlobalValidator.dataRequireNonNull(
                vc.getVillaById(id),
                "Villa dengan id " + id + " tidak ditemukan"
        );

        ResponseController.sendJsonResponse(villa, res);
    }

    public static void handleGetRoomsByVillaId(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        VillasController vc = new VillasController();

        List<RoomTypes> rt = GlobalValidator.listRequireNotEmpty(
                vc.getRoomsByVillaId(id),
                "Tidak ada data ruangan pada vila dengan id " + id
        );

        ResponseController.sendJsonResponse(rt, res);
    }

    public static void handleGetBookingsByVillaId(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        VillasController vc = new VillasController();

        List<Booking> bookings = GlobalValidator.listRequireNotEmpty(
                vc.getBookingsByVillaId(id),
                "Tidak ada daftar booking pada vila dengan id " + id
        );

        ResponseController.sendJsonResponse(bookings, res);
    }

    public static void handleGetReviewsByVillaId(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        ReviewController rc = new ReviewController();

        List<Review> reviews = GlobalValidator.listRequireNotEmpty(
                rc.getReviewsByVillaId(id),
                "Villa dengan id " + id + " tidak memiliki review"
        );

        ResponseController.sendJsonResponse(reviews, res);
    }

    public static void handleGetAllCustomers(Response res) throws Exception {
        CustomerController cc = new CustomerController();

        List<Customer> customers = GlobalValidator.listRequireNotEmpty(
                cc.getAllCustomers(),
                "Tidak ada data customer yang tersedia"
        );

        ResponseController.sendJsonResponse(customers, res);
    }

    public static void handleGetCustomerById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        CustomerController cc = new CustomerController();

        Customer customer = GlobalValidator.dataRequireNonNull(
                cc.getCustomerById(id),
                "Customer dengan id " + id + " tidak ditemukan"
        );

        ResponseController.sendJsonResponse(customer, res);
    }

    public static void handleGetCustomerBookingsById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        CustomerController cc = new CustomerController();

        List<Booking> bookings = GlobalValidator.listRequireNotEmpty(
                cc.getCustomerBookings(id),
                "Tidak menemukan data booking pada customer dengan id " + id
        );

        ResponseController.sendJsonResponse(bookings, res);
    }

    public static void handleGetCustomerReviewsById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        ReviewController rc = new ReviewController();

        List<Review> reviews = GlobalValidator.listRequireNotEmpty(
                rc.getReviewsByCustomerId(id),
                "Belum ada review untuk customer dengan id " + id);

        ResponseController.sendJsonResponse(reviews, res);
    }

    public static void handleGetAllVouchers(Response res) throws Exception {
        VoucherController vc = new VoucherController();

        List<Voucher> vouchers = GlobalValidator.listRequireNotEmpty(
                vc.getAllVouchers(),
                "Tidak ada data voucher yang tersedia"
                );

        ResponseController.sendJsonResponse(vouchers, res);
    }

    public static void handleGetVoucherById(String path, Response res) throws Exception {
        int id = extractIdFromPath(path, 1);
        VoucherController vc = new VoucherController();

        Voucher voucher = GlobalValidator.dataRequireNonNull(
                vc.getVoucherById(id),
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

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil membuat villa",
                villa,
                res
        );
    }

    public static void handlePostRoomTypeByVillaId(String path, Response res, Request req) throws Exception {
        int id = extractIdFromPath(path, 2);
        VillasController vc = new VillasController();

        GlobalValidator.dataRequireNonNull(
                vc.getVillaById(id),
                "Villa dengan id " + id + " tidak ditemukan"
        );

        String body = req.getBody();
        RoomTypes rt = mapper.readValue(body, RoomTypes.class);

        VillaValidator.validatePostVillaRooms(rt);
        rt.setVilla_id(id);
        vc.createVillasRooms(rt);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil membuat room type di villa dengan id " + id,
                rt,
                res
        );
    }

    public static void handlePostCustomer(Response res, Request req) throws Exception{
        CustomerController cc = new CustomerController();

        String body = req.getBody();
        Customer customer = mapper.readValue(body, Customer.class);

        CustomerValidator.validatePostCustomer(customer);
        cc.postCustomer(customer);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil membuat customer",
                customer,
                res
        );
    }

    public static void handlePostBookingByCustomerId(String path, Response res, Request req) throws Exception {
        int id = extractIdFromPath(path, 2);
        CustomerController cc = new CustomerController();

        String body = req.getBody();
        Booking booking = mapper.readValue(body, Booking.class);

        booking.setCustomer(id);
        cc.postBookingForCustomer(booking, id);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil membuat booking di costumer dengan id " + id,
                booking,
                res
        );
    }

    public static void handlePostReviewByCustomerAndBookingId(String path, Response res, Request req) throws Exception {
        int bookingId = extractIdFromPath(path, 2);
        int customerId = extractIdFromPath(path, 4);

        ReviewValidator.checkBookingBelongsToCustomer(bookingId, customerId);

        String body = req.getBody();
        Review review = mapper.readValue(body, Review.class);

        ReviewValidator.validatePostReview(review);
        review.setBooking(bookingId);

        ReviewController rc = new ReviewController();
        rc.postReviewForBooking(review, customerId, bookingId);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil membuat review untuk customer dengan id " + customerId + " dan booking dengan id " + bookingId,
                review,
                res
        );
    }

    public static void handlePostVoucher(Response res, Request req) throws Exception {
        String body = req.getBody();
        Voucher voucher = mapper.readValue(body, Voucher.class);

        VoucherValidator.validatePostVoucher(voucher);

        VoucherController vc = new VoucherController();
        vc.postVoucher(voucher);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil membuat voucher",
                voucher,
                res);
    }




    //PUT
    public static void handlePutVillaById(String path, Response res, Request req) throws Exception {
        int id = extractIdFromPath(path, 1);
        VillasController vc = new VillasController();

        Villas oldVilla = GlobalValidator.dataRequireNonNull(
                vc.getVillaById(id),
                "Villa dengan id " + id + " tidak ditemukan"
        );

        String body = req.getBody();
        Villas newVilla = mapper.readValue(body, Villas.class);

        VillaValidator.validatePostVilla(newVilla);
        newVilla.setId(id);

        vc.updateVilla(newVilla);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil mengupdate villa dengan id " + id,
                oldVilla,
                newVilla,
                res
        );
    }

    public static void handlePutRoomTypeByVillaId(String path, Response res, Request req) throws Exception {
        int rid = extractIdFromPath(path, 1);
        int vid = extractIdFromPath(path, 3);
        VillasController vc = new VillasController();

        RoomTypes oldRoomType = GlobalValidator.dataRequireNonNull(
                vc.getVillaRoomById(rid,vid),
                "ID Villa atau Room Type tidak ditemukan"
        );

        String body = req.getBody();
        RoomTypes newRoomType = mapper.readValue(body, RoomTypes.class);

        newRoomType.setIdAndVillaId(rid, vid);
        vc.updateVillasRoomTypes(newRoomType);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil mengupdate roomtype dengan id " + rid + " di villa dengan id " + vid,
                oldRoomType,
                newRoomType,
                res
        );
    }

    public static void handlePutCustomerById(String path, Response res, Request req) throws Exception {
        int id = extractIdFromPath(path, 1);
        CustomerController cc = new CustomerController();

        Customer oldCustomer = GlobalValidator.dataRequireNonNull(
                cc.getCustomerById(id),
                "Customer dengan id " + id + " tidak ditemukan"
        );

        String body = req.getBody();
        Customer newCustomer = mapper.readValue(body, Customer.class);

        newCustomer.setId(id);
        cc.updateCustomer(newCustomer);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil mengupdate customer dengan id " + id,
                oldCustomer,
                newCustomer,
                res
        );
    }

    public static void handlePutVoucherById(String path, Response res, Request req) throws Exception {
        int id = extractIdFromPath(path, 1);
        VoucherController vc = new VoucherController();

        Voucher oldVoucher = GlobalValidator.dataRequireNonNull(
                vc.getVoucherById(id),
                "Voucher dengan id " + id + " tidak ditemukan"
        );

        String body = req.getBody();
        Voucher newVoucher = mapper.readValue(body, Voucher.class);

        newVoucher.setId(id);
        vc.updateVoucher(newVoucher);

        ResponseController.sendJsonResponseWithMessage(
                "Berhasil mengupdate voucher dengan id " + id,
                oldVoucher,
                newVoucher,
                res);
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