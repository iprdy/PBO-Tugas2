package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import models.Customer;
import models.Booking;
import java.sql.*;
import java.util.*;

public class CustomerController {

    private Connection conn;

    public CustomerController(Connection conn) {
        this.conn = conn;
    }

    //GET /customer/{id}/bookings => daftar booking yang telah dilakukan oleh seorang customer
    public void getCustomerBookings(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            // Ambil ID dari path
            String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
            int customerId = Integer.parseInt(pathParts[2]);

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


            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(bookings);
            res.json(json);

        } catch (Exception e) {
            res.error("Failed to fetch bookings: " + e.getMessage());
        }
    }

    public void getAllCustomers(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
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

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(customers);
            res.json(json);

        } catch (Exception e) {
            res.error("Failed to fetch customers: " + e.getMessage());
        }
    }
}
