package controllers;

import models.Villas;

import java.sql.*;

public class VillasController {

    //POST /villas /menambahkan data vila
    public Villas createVilla(Villas villa) throws SQLException {
        String sql = "INSERT INTO villas (id, name, description, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println("Has connected to the database");

            ps.setString(1, villa.getName());
            ps.setString(2, villa.getDescription());
            ps.setString(3, villa.getAddress());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create villa");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    villa.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Failed to create villa, no ID obtained");
                }
            }

            return villa;

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            throw e;
        }
    }
}
