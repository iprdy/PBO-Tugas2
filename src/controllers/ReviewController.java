package controllers;

import database.DatabaseBuilder;
import models.Review;

import java.sql.*;
import java.util.*;

public class ReviewController {
    public void postReviewForBooking(Review reviewData, int customerId, int bookingId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL)) {
            String insertSql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setInt(1, reviewData.getBooking());
            ps.setInt(2, reviewData.getStar());
            ps.setString(3, reviewData.getTitle());
            ps.setString(4, reviewData.getContent());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Gagal menambahkan review");
            }
        }
    }

    public List<Review> getReviewsByVillaId(int villaId) throws SQLException {
        String sql = """
            SELECT r.booking, r.star, r.title, r.content
            FROM reviews r
            JOIN bookings b ON r.booking = b.id
            JOIN room_types rt ON b.room_type = rt.id
            WHERE rt.villa = ?
        """;

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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

            return reviews.isEmpty() ? null : reviews;
        }
    }

    public List<Review> getReviewsByCustomerId(int customerId) throws SQLException {
        String sql = """
            SELECT r.booking, r.star, r.title, r.content
            FROM reviews r
            JOIN bookings b ON r.booking = b.id
            WHERE b.customer = ?
        """;

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
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

            return reviews;
        }
    }
}