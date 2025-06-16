package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import models.Customer;
import models.Booking;
import models.Review;
import java.sql.*;
import java.util.*;

public class CustomerController {
    //GET /customer/{id}/bookings => daftar booking yang telah dilakukan oleh seorang customer
    public List<Booking> getCustomerBookings(int customerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {
            String sql = "SELECT * FROM bookings WHERE customer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            List<Booking> bookings = new ArrayList<>();
            while (rs.next()) {
                Integer voucher = rs.getObject("voucher") != null ? rs.getInt("voucher") : null;

                Booking booking = new Booking(
                        rs.getInt("customer"),
                        rs.getInt("room_type"),
                        rs.getString("checkin_date"),
                        rs.getString("checkout_date"),
                        rs.getInt("price"),
                        voucher,
                        rs.getInt("final_price")
                );

                booking.setId(rs.getInt("id"));
                booking.setPaymentStatus(rs.getString("payment_status"));
                booking.setHasCheckedin(rs.getBoolean("has_checkedin"));
                booking.setHasCheckedout(rs.getBoolean("has_checkedout"));
                bookings.add(booking);
            }
            return bookings;
        }
    }

    // POST /customer/{id}/bookings => Customer melakukan pemesanan vila
    public void postBookingForCustomer(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {
            // Ambil customer ID dari URL
            String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
            int customerId = Integer.parseInt(pathParts[2]);

            // Ambil body dari request dan parsing jadi objek Booking
            ObjectMapper objectMapper = new ObjectMapper();
            Booking bookingData = objectMapper.readValue(req.getBody(), Booking.class);

            // Siapkan SQL INSERT
            String sql = """
            INSERT INTO bookings (customer, room_type, checkin_date, checkout_date, price, voucher, final_price, payment_status, has_checkedin, has_checkedout)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, customerId);
            ps.setInt(2, bookingData.getRoomType());
            ps.setString(3, bookingData.getCheckinDate());
            ps.setString(4, bookingData.getCheckoutDate());
            ps.setInt(5, bookingData.getPrice());

            if (bookingData.getVoucher() != null) {
                ps.setInt(6, bookingData.getVoucher());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setInt(7, bookingData.getFinalPrice());
            ps.setString(8, "waiting"); // default
            ps.setBoolean(9, false);    // default hasCheckedin
            ps.setBoolean(10, false);   // default hasCheckedout

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                res.error("Failed to insert booking");
                return;
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                bookingData.setId(newId);
            }

            bookingData.setPaymentStatus("waiting");
            bookingData.setHasCheckedin(false);
            bookingData.setHasCheckedout(false);
            bookingData.setId(bookingData.getId()); // agar id terisi

            String json = objectMapper.writeValueAsString(bookingData);
            res.json(json);

        } catch (Exception e) {
            res.error("Failed to create booking: " + e.getMessage());
        }
    }


    public List<Customer> getAllCustomers() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {
            String sql = "SELECT * FROM customers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                customers.add(customer);
            }
            return customers;
        }
    }

    // GET /customers/{id} -> detail satu customer
    public Customer getCustomerById(int customerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {
            Customer customer = new Customer();
            String sql = "SELECT * FROM customers WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                 customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
            return customer;
        }
    }

    // POST /customers -> menambahkan customer baru
    public void postCustomer(HttpExchange httpExchange) {
        System.out.println(">>> postCustomer() triggered");
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Customer customer = objectMapper.readValue(req.getBody(), Customer.class);

            System.out.println("Parsed customer: " + customer.getName());

            String sql = "INSERT INTO customers (id, name, email, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());

            int rows = ps.executeUpdate();
            System.out.println("Inserted rows: " + rows);

            if (rows > 0) {
                res.json("{\"message\": \"Customer added successfully\"}");
            } else {
                res.error("Failed to insert customer");
            }
        } catch (Exception e) {
            System.out.println("Error in postCustomer: " + e.getMessage());
            res.error("Failed to create customer: " + e.getMessage());
        }
    }



    // PUT /customers/{id} -> mengubah data customer berdasarkan ID
    public void updateCustomer(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {
            // Ambil ID dari path URL
            String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
            int customerId = Integer.parseInt(pathParts[2]);

            // Ambil body request dan parsing jadi Customer
            ObjectMapper objectMapper = new ObjectMapper();
            Customer updatedCustomer = objectMapper.readValue(req.getBody(), Customer.class);

            String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, updatedCustomer.getName());
            ps.setString(2, updatedCustomer.getEmail());
            ps.setString(3, updatedCustomer.getPhone());
            ps.setInt(4, customerId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                res.error("Customer with ID " + customerId + " not found or no changes made.");
                return;
            }

            updatedCustomer.setId(customerId); // Set ID untuk response
            String json = objectMapper.writeValueAsString(updatedCustomer);
            res.json(json);

        } catch (Exception e) {
            res.error("Failed to update customer: " + e.getMessage());
        }
    }

    // GET /customers/{id}/reviews -> daftar review yang diberikan oleh customer
    public List<Review> getReviewsByCustomerId(int customerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db")) {
            // SQL: cari review yang berelasi dengan booking milik customer ini
            String sql = """
                SELECT r.booking, r.star, r.title, r.content
                FROM reviews r
                JOIN bookings b ON r.booking = b.id
                WHERE b.customer = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

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
}
