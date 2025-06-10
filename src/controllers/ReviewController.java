package controllers;

import com.sun.net.httpserver.HttpExchange;
import models.Review;
import utils.JsonUtils;
import utils.RequestValidator;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ReviewController {
    private final Connection conn;

    public ReviewController(Connection conn) {
        this.conn = conn;
    }

    // GET /bookings/{bookingId}/review
    public void getReviewByBooking(HttpExchange exchange, int bookingId) throws IOException {
        try {
            String sql = "SELECT * FROM reviews WHERE booking = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Review review = new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                );
                JsonUtils.sendJsonResponse(exchange, 200, review);
            } else {
                JsonUtils.sendError(exchange, 404, "Review tidak ditemukan");
            }
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal mengambil review");
        }
    }

    // POST /bookings/{bookingId}/review
    public void addReview(HttpExchange exchange, int bookingId) throws IOException {
        try {
            Map<String, Object> body = JsonUtils.parseJson(exchange.getRequestBody());
            String title = (String) body.get("title");
            String content = (String) body.get("content");
            int star = (int) body.get("star");

            if (!RequestValidator.isValidRating(star) || title == null || title.isBlank() || content == null || content.isBlank()) {
                JsonUtils.sendError(exchange, 400, "Data review tidak valid");
                return;
            }

            String sql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            stmt.setInt(2, star);
            stmt.setString(3, title);
            stmt.setString(4, content);
            stmt.executeUpdate();

            JsonUtils.sendJsonResponse(exchange, 201, Map.of("message", "Review berhasil ditambahkan"));
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal menambahkan review");
        }
    }

    // PUT /reviews/{bookingId}
    public void updateReview(HttpExchange exchange, int bookingId) throws IOException {
        try {
            Map<String, Object> body = JsonUtils.parseJson(exchange.getRequestBody());
            String title = (String) body.get("title");
            String content = (String) body.get("content");
            int star = (int) body.get("star");

            if (!RequestValidator.isValidRating(star) || title == null || title.isBlank() || content == null || content.isBlank()) {
                JsonUtils.sendError(exchange, 400, "Data review tidak valid");
                return;
            }

            String sql = "UPDATE reviews SET star = ?, title = ?, content = ? WHERE booking = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, star);
            stmt.setString(2, title);
            stmt.setString(3, content);
            stmt.setInt(4, bookingId);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                JsonUtils.sendError(exchange, 404, "Review tidak ditemukan");
            } else {
                JsonUtils.sendJsonResponse(exchange, 200, Map.of("message", "Review berhasil diperbarui"));
            }
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal memperbarui review");
        }
    }

    // DELETE /reviews/{bookingId}
    public void deleteReview(HttpExchange exchange, int bookingId) throws IOException {
        try {
            String sql = "DELETE FROM reviews WHERE booking = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                JsonUtils.sendError(exchange, 404, "Review tidak ditemukan");
            } else {
                JsonUtils.sendJsonResponse(exchange, 200, Map.of("message", "Review berhasil dihapus"));
            }
        } catch (Exception e) {
            JsonUtils.sendError(exchange, 500, "Gagal menghapus review");
        }
    }
}
