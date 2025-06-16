package controllers;

import api.Response;
import models.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouterController {
    public static void handleGetAllVilla(Response res) {
        try {
            VillasController vc = new VillasController();
            List<Villas> villa = vc.getAllVillas();

            ResponseController.sendJsonResponse(villa, res);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetVillaById(String path, Response res) throws SQLException {
        String[] split = path.split("/");
        int id = Integer.parseInt(split[2]);
        VillasController vc = new VillasController();
        Villas villa = vc.getVillaById(id);

        ResponseController.sendJsonResponse(villa, res);
    }

    public static void handleGetVillaIdRooms(String path, Response res) throws SQLException {
        String[] split = path.split("/");
        int id = Integer.parseInt(split[2]);
        VillasController vc = new VillasController();
        List<RoomTypes> rt = vc.getRoomsByVillaId(id);

        ResponseController.sendJsonResponse(rt, res);
    }

    public static void handleGetVillaIdBookings(String path, Response res) throws SQLException {
        String[] split = path.split("/");
        int id = Integer.parseInt(split[2]);
        VillasController vc = new VillasController();
        List<Booking> bookings = vc.getBookingsByVillaId(id);

        ResponseController.sendJsonResponse(bookings, res);
    }

    public static void handleGetVillaIdReviews(String path, Response res) throws SQLException {
        String[] split = path.split("/");
        int id = Integer.parseInt(split[2]);
        VillasController vc = new VillasController();
        List<Review> reviews = vc.getReviewsByVillaId(id);

        ResponseController.sendJsonResponse(reviews, res);
    }

    public static void handleGetVillaCinCout(String path, Response res) throws SQLException {}

    public static void handleGetAllCustomer(Response res) throws SQLException {
        CustomerController cc = new CustomerController();
        List<Customer> customers = cc.getAllCustomers();

        ResponseController.sendJsonResponse(customers, res);
    }
}
