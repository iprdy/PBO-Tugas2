package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            VillasController vc = new VillasController();
            List<Review> reviews = vc.getReviewsByVillaId(id);

            ResponseController.sendJsonResponse(reviews, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetVillaCinCout(String path, Response res) {}

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
            CustomerController cc = new CustomerController();
            List<Review> reviews = cc.getReviewsByCustomerId(id);

            ResponseController.sendJsonResponse(reviews, res);
        } catch (NumberFormatException e) {
            ResponseController.sendErrorResponse(res, "ID tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (SQLException e) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (Exception e) {
            ResponseController.sendErrorResponse(res, "Unexpected error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    public static void handleGetAllVouchers(Response res) throws SQLException {

    }

    public static void handleGetVoucherById(String path, Response res) throws SQLException {

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

    public static void handlePostCustomer(Response res) throws SQLException {

    }

    public static void handlePostCustomerIdBookings(String path, Response res) throws SQLException {

    }

    public static void handlePostCustomerIdBookingsIdReviews(String path, Response res) throws SQLException {

    }

    public static void handlePostVouchers(Response res) throws SQLException {

    }
}
