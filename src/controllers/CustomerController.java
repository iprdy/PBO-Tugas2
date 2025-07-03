package controllers;

import database.DatabaseBuilder;

import models.Customer;

import models.Booking;
import java.sql.*;
import java.util.*;

public class CustomerController {

    //GET /customer/{id}/bookings => daftar booking yang telah dilakukan oleh seorang customer
    public List<Booking> getCustomerBookings(int customerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL)) {
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
    public void postBookingForCustomer(Booking bookingData, int customerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL)) {
            String sql = """
            INSERT INTO bookings (
                customer, room_type, checkin_date, checkout_date,
                price, voucher, final_price, payment_status,
                has_checkedin, has_checkedout
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, customerId);
            ps.setInt(2, bookingData.getRoomType());
            ps.setString(3, bookingData.getCheckinDate());
            ps.setString(4, bookingData.getCheckoutDate());
            ps.setInt(5, bookingData.getPrice());

            // Null handling untuk voucher
            if (bookingData.getVoucher() != null) {
                ps.setInt(6, bookingData.getVoucher());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setInt(7, bookingData.getFinalPrice());
            ps.setString(8, "waiting"); // default payment status
            ps.setBoolean(9, false);    // has_checkedin default false
            ps.setBoolean(10, false);   // has_checkedout default false

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create booking");
            }

            // Ambil id yang baru dibuat (auto-increment)
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                bookingData.setId(newId); // simpan ke objek
            }

            // Pastikan field default tetap terisi di objek Java
            bookingData.setPaymentStatus("waiting");
            bookingData.setHasCheckedin(false);
            bookingData.setHasCheckedout(false);
        }
    }




    public List<Customer> getAllCustomers() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL)) {
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
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL)) {
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
    public void postCustomer(Customer customer) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL)) {
            String sql = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create customer");
            }
        }
    }



    // PUT /customers/{id} -> mengubah data customer berdasarkan ID
    public void updateCustomer(Customer updatedCustomer) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL)) {
            String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, updatedCustomer.getName());
            ps.setString(2, updatedCustomer.getEmail());
            ps.setString(3, updatedCustomer.getPhone());
            ps.setInt(4, updatedCustomer.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update customer");
            }
        }
    }
}