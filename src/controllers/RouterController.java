package controllers;

import api.Response;
import models.Booking;
import models.RoomTypes;
import models.Villas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouterController {
    public static void handleGetAllVilla(Response res) throws SQLException {
        VillasController vc = new VillasController();
        List<Villas> villa = vc.getAllVillas();

        ResponseController.sendJsonResponse(villa, res);
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
        vc.getReviewsByVillaId(id);
    }
}
