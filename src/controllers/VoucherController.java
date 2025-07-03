package controllers;


import database.DatabaseBuilder;

import models.Voucher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherController {
    public List<Voucher> getAllVouchers() throws SQLException {
        List<Voucher> vouchers = new ArrayList<>();
        String sql = "SELECT * FROM vouchers";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Voucher voucher = new Voucher(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("discount"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
                vouchers.add(voucher);
            }
            return vouchers;
        }
    }

    public Voucher getVoucherById(int id) throws SQLException {
        String sql = "SELECT * FROM vouchers WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Voucher(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("discount"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
            } else {
                return null; // tidak ditemukan
            }
        }
    }

    public void postVoucher(Voucher voucher) throws SQLException {
        String sql = "INSERT INTO vouchers (code, description, discount, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voucher.getCode());
            stmt.setString(2, voucher.getDescription());
            stmt.setDouble(3, voucher.getDiscount());
            stmt.setString(4, voucher.getStartDate());
            stmt.setString(5, voucher.getEndDate());

            stmt.executeUpdate();
        }
    }

    public void updateVoucher(Voucher voucher) throws SQLException {
        String sql = "UPDATE vouchers SET code = ?, description = ?, discount = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voucher.getCode());
            stmt.setString(2, voucher.getDescription());
            stmt.setDouble(3, voucher.getDiscount());
            stmt.setString(4, voucher.getStartDate());
            stmt.setString(5, voucher.getEndDate());
            stmt.setInt(6, voucher.getId());

            stmt.executeUpdate();
        }
    }

    public void deleteVoucher(int id) throws SQLException {
        String sql = "DELETE FROM vouchers WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}