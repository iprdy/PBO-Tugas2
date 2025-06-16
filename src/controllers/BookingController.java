package controllers;

import api.Request;
import api.Response;
import models.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BookingController {

    public static void create(Request req, Response res) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            String sql = "INSERT INTO bookings ";
            PreparedStatement ps = conn.prepareStatement(sql);

            ObjectMapper mapper = new ObjectMapper();
            Booking booking = mapper.readValue(req.getBody(), Booking.class);

//            ps.setString(1, booking.getGuest());
//            ps.setString(2, booking.getVilla());
//            ps.setString(3, booking.getDate());

            ps.executeUpdate();
            res.send("Booking berhasil dibuat");
        } catch (Exception e) {
            res.send("Booking tidak berhasil dibuat: " + e.getMessage());
        }
    }

    public static void getAll(Request req, Response res) {
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM bookings");

            while (rs.next()) {
//                Booking b = new Booking(
//                        rs.getInt("id"),
//                        rs.getString("guest"),
//                        rs.getString("villa"),
//                        rs.getString("date")
//                );
//                bookings.add(b);
            }

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(bookings);
            res.json(json);
        } catch (Exception e) {
            res.send("Failed to retrieve bookings: " + e.getMessage());
        }
    }

    public static void getById(Request req, Response res) {
        int id = Integer.parseInt(req.getParam("id"));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            String sql = "SELECT * FROM bookings WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
//                Booking b = new Booking(
//                        rs.getInt("id"),
//                        rs.getString("guest"),
//                        rs.getString("villa"),
//                        rs.getString("date")
//                );
                ObjectMapper mapper = new ObjectMapper();
//                res.json(mapper.writeValueAsString(b));
            } else {
                res.send("Booking tidak ditemukan");
            }
        } catch (Exception e) {
            res.send("TIdak berhasil mengambil booking: " + e.getMessage());
        }
    }
}
