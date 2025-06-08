package controllers;

import com.sun.net.httpserver.HttpExchange;
import models.Review;
import utils.JsonUtils;
import utils.RequestValidator;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ReviewController {
    private Connection conn;

    public ReviewController(Connection conn) {
        this.conn = conn;
    }

    // GET /villas/{villaId}/reviews
    public void getReviewsByVilla(HttpExchange exchange, int villaId) throws IOException {
        try {
            List<Review> reviews = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reviews WHERE villa_id = ?");
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getInt("villa_id"),
                        rs.getInt("booking_id"),
                        rs.getString("comment"),
                        rs.getInt("rating")
                ));
            }

            JsonUtils.sendJsonResponse(exchange, 200, reviews);
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal mengambil review");
        }
    }

    // GET /customers/{customerId}/reviews
    public void getReviewsByCustomer(HttpExchange exchange, int customerId) throws IOException {
        try {
            List<Review> reviews = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reviews WHERE customer_id = ?");
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getInt("villa_id"),
                        rs.getInt("booking_id"),
                        rs.getString("comment"),
                        rs.getInt("rating")
                ));
            }

            JsonUtils.sendJsonResponse(exchange, 200, reviews);
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal mengambil review customer");
        }
    }

    // POST /customers/{cid}/bookings/{bid}/reviews
    public void addReview(HttpExchange exchange, int customerId, int bookingId, int villaId) throws IOException {
        try {
            Map<String, Object> body = JsonUtils.parseJson(exchange.getRequestBody());
            String comment = (String) body.get("comment");
            int rating = (int) body.get("rating");

            if (!RequestValidator.isValidRating(rating) || comment == null || comment.trim().isEmpty()) {
                JsonUtils.sendError(exchange, 400, "Data review tidak valid");
                return;
            }

            String sql = "INSERT INTO reviews (customer_id, villa_id, booking_id, comment, rating) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            stmt.setInt(2, villaId);
            stmt.setInt(3, bookingId);
            stmt.setString(4, comment);
            stmt.setInt(5, rating);
            stmt.executeUpdate();

            JsonUtils.sendJsonResponse(exchange, 201, Map.of("message", "Review berhasil ditambahkan"));
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal menambahkan review");
        }
    }

    // PUT /reviews/{id}
    public void updateReview(HttpExchange exchange, int reviewId) throws IOException {
        try {
            Map<String, Object> body = JsonUtils.parseJson(exchange.getRequestBody());
            String comment = (String) body.get("comment");
            int rating = (int) body.get("rating");

            if (!RequestValidator.isValidRating(rating) || comment == null || comment.trim().isEmpty()) {
                JsonUtils.sendError(exchange, 400, "Data update review tidak valid");
                return;
            }

            String sql = "UPDATE reviews SET comment = ?, rating = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, comment);
            stmt.setInt(2, rating);
            stmt.setInt(3, reviewId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                JsonUtils.sendError(exchange, 404, "Review tidak ditemukan");
                return;
            }

            JsonUtils.sendJsonResponse(exchange, 200, Map.of("message", "Review berhasil diupdate"));
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal update review");
        }
    }

    // DELETE /reviews/{id}
    public void deleteReview(HttpExchange exchange, int reviewId) throws IOException {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM reviews WHERE id = ?");
            stmt.setInt(1, reviewId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted == 0) {
                JsonUtils.sendError(exchange, 404, "Review tidak ditemukan");
                return;
            }

            JsonUtils.sendJsonResponse(exchange, 200, Map.of("message", "Review berhasil dihapus"));
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal menghapus review");
        }
    }
}
