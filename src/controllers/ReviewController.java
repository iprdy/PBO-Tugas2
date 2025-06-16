package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import models.Review;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.HttpURLConnection;

public class ReviewController {
    private Connection conn;

    public ReviewController(Connection conn) {
        this.conn = conn;
    }

    // GET villas/id/reviews
    public void getReviewsByVillaId(HttpExchange exchange, int villaId) throws IOException {
        Response res = new Response(exchange);
        String sql = """
        SELECT r.booking, r.star, r.title, r.content
        FROM reviews r
        JOIN bookings b ON r.booking = b.id
        JOIN room_types rt ON b.room_type = rt.id
        WHERE rt.villa = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();

            List<Review> reviews = new ArrayList<>();
            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                ));
            }

            if (!res.isSent()) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resJsonMap = new HashMap<>();
                resJsonMap.put("message", "Request success");
                resJsonMap.put("data", reviews);

                String resJson = "";
                try {
                    resJson = objectMapper.writeValueAsString(resJsonMap);
                } catch (Exception e) {
                    System.out.println("Serialization error: " + e.getMessage());
                }

                res.setBody(resJson);
                res.send(HttpURLConnection.HTTP_OK);
            }

        } catch (SQLException e) {
            res.setBody("{\"message\":\"Database error: " + e.getMessage() + "\"}");
            res.send(HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    //  GET customers/id/reviews
    public void getReviewsByCustomerId(HttpExchange exchange, int customerId) throws IOException {
        Response res = new Response(exchange);
        String sql = """
        SELECT r.booking, r.star, r.title, r.content
        FROM reviews r
        JOIN bookings b ON r.booking = b.id
        WHERE b.customer = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            List<Review> reviews = new ArrayList<>();
            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                );
                reviews.add(review);
            }

            if (!res.isSent()) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resJsonMap = new HashMap<>();
                resJsonMap.put("message", "Request success");
                resJsonMap.put("data", reviews);

                String resJson = "";
                try {
                    resJson = objectMapper.writeValueAsString(resJsonMap);
                } catch (Exception e) {
                    System.out.println("Serialization error: " + e.getMessage());
                }

                res.setBody(resJson);
                res.send(HttpURLConnection.HTTP_OK);
            }

        } catch (SQLException e) {
            res.setBody("{\"message\": \"Database error: " + e.getMessage() + "\"}");
            res.send(HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    // POST customers/id/booking/id/reviews
    public void postReviewForBooking(HttpExchange httpExchange, int customerId, int bookingId) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {

            ObjectMapper mapper = new ObjectMapper();
            Review reviewData = mapper.readValue(req.getBody(), Review.class);

            String checkSql = "SELECT * FROM bookings WHERE id = ? AND customer = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, bookingId);
            checkStmt.setInt(2, customerId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                res.error("Booking not found or doesn't belong to customer.");
                return;
            }

            String insertSql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setInt(1, bookingId);
            ps.setInt(2, reviewData.getStar());
            ps.setString(3, reviewData.getTitle());
            ps.setString(4, reviewData.getContent());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                res.error("Failed to insert review");
                return;
            }

            res.json("{\"message\": \"Review successfully added\"}");

        } catch (Exception e) {
            res.error("Failed to create review: " + e.getMessage());
        }
    }

}