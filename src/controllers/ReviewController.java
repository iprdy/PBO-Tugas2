package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewController {

    private Connection conn;

    public ReviewController(Connection conn) {
        this.conn = conn;
    }

    public void getReviewsByVillaId(HttpExchange httpExchange, int villaId) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try {
            String sql = "SELECT * FROM reviews WHERE booking = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, villaId);
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

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(reviews);
            res.json(json);
        } catch (Exception e) {
            res.error(e.getMessage());
        }
    }

    public static void create(Request req, Response res) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            Map<String, Object> json = req.getJSON();

            int booking = (int) json.get("booking");
            int star = (int) json.get("star");
            String title = (String) json.get("title");
            String content = (String) json.get("content");

            Review review = new Review(booking, star, title, content);

            String sql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, review.getBooking());
            stmt.setInt(2, review.getStar());
            stmt.setString(3, review.getTitle());
            stmt.setString(4, review.getContent());
            stmt.executeUpdate();

            res.send("Review created");
        } catch (Exception e) {
            res.error(e.getMessage());
        }
    }

    public static void getAll(Request req, Response res) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            String sql = "SELECT * FROM reviews";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

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

            ObjectMapper objectMapper = new ObjectMapper();
            res.json(objectMapper.writeValueAsString(reviews));
        } catch (Exception e) {
            res.error(e.getMessage());
        }
    }

    public static void getOne(Request req, Response res) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            int booking = Integer.parseInt(req.getParam("booking"));
            String sql = "SELECT * FROM reviews WHERE booking = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, booking);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Review review = new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                );
                ObjectMapper objectMapper = new ObjectMapper();
                res.json(objectMapper.writeValueAsString(review));
            } else {
                res.send("Review not found");
            }
        } catch (Exception e) {
            res.error(e.getMessage());
        }
    }

    public static void update(Request req, Response res) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            Map<String, Object> json = req.getJSON();

            int booking = (int) json.get("booking");
            int star = (int) json.get("star");
            String title = (String) json.get("title");
            String content = (String) json.get("content");

            String sql = "UPDATE reviews SET star = ?, title = ?, content = ? WHERE booking = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, star);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.setInt(4, booking);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                res.send("Review updated");
            } else {
                res.send("Review not found");
            }
        } catch (Exception e) {
            res.error(e.getMessage());
        }
    }

    public static void delete(Request req, Response res) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            int booking = Integer.parseInt(req.getParam("booking"));
            String sql = "DELETE FROM reviews WHERE booking = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, booking);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                res.send("Review deleted");
            } else {
                res.send("Review not found");
            }
        } catch (Exception e) {
            res.error(e.getMessage());
        }
    }
}
