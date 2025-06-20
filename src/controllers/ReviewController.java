package controllers;

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

    public ReviewController() {

    }

    // GET villas/id/reviews
    public List<Review> getReviewsByVillaId(int villaId) throws SQLException {
        String sql = """
        SELECT r.booking, r.star, r.title, r.content
        FROM reviews r
        JOIN bookings b ON r.booking = b.id
        JOIN room_types rt ON b.room_type = rt.id
        WHERE rt.villa = ?
    """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
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
            return reviews;
        }
    }

    //  GET customers/id/reviews
    public List<Review> getReviewsByCustomerId (int customerId) throws SQLException {
        String sql = """
        SELECT r.booking, r.star, r.title, r.content
        FROM reviews r
        JOIN bookings b ON r.booking = b.id
        WHERE b.customer = ?
    """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            return reviews;
        }
    }

    // POST customers/id/booking/id/reviews
    public void postReviewForBooking(Review reviewData, int customerId, int bookingId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DBConfig.DB_URL)) {
            String checkSql = "SELECT * FROM bookings WHERE id = ? AND customer = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, bookingId);
            checkStmt.setInt(2, customerId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Booking not found or doesn't belong to customer.");
            }

            // ini agar field booking tidak 0 di response
            reviewData.setBooking(bookingId);

            String insertSql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setInt(1, bookingId);
            ps.setInt(2, reviewData.getStar());
            ps.setString(3, reviewData.getTitle());
            ps.setString(4, reviewData.getContent());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Failed to create review");
            }
        }
    }
}