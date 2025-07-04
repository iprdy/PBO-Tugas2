package controllers;

import database.DatabaseBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import exceptions.DataNotFoundException;
import models.RoomTypes;
import models.Booking;
import models.Villas;
import java.sql.*;

public class VillasController {
    // GET /villas => Menampilkan semua data vila
    public List<Villas> getAllVillas() throws SQLException {
        List<Villas> villas = new ArrayList<>();
        String sql = "SELECT * FROM villas";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Villas villa = new Villas(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
                villas.add(villa);
            }
        }
        return villas.isEmpty() ? null : villas;
    }

    // GET /villas/{id} => Menampilkan detail data satu vila
    public Villas getVillaById(int id) throws SQLException {
        String sql = "SELECT * FROM villas WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Villas(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
            } else {
                return null; // Bisa dilempar sebagai Exception oleh pemanggil jika null
            }
        }
    }

    // GET /villas/{id}/rooms => Menampilkan semua kamar dari suatu vila
    public List<RoomTypes> getRoomsByVillaId(int villaId) throws SQLException {
        List<RoomTypes> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room_types WHERE villa = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, villaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RoomTypes room = new RoomTypes(
                        rs.getInt("id"),
                        rs.getInt("villa"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("capacity"),
                        rs.getInt("price"),
                        rs.getString("bed_size"),
                        rs.getBoolean("has_desk"),
                        rs.getBoolean("has_ac"),
                        rs.getBoolean("has_tv"),
                        rs.getBoolean("has_wifi"),
                        rs.getBoolean("has_shower"),
                        rs.getBoolean("has_hotwater"),
                        rs.getBoolean("has_fridge")
                );
                rooms.add(room);
            }
        }
        return rooms;
    }

    // GET /villas/{id}/bookings => Menampilkan semua booking pada suatu vila
    public List<Booking> getBookingsByVillaId(int villaId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.* FROM bookings b
            JOIN room_types rt ON b.room_type = rt.id
            WHERE rt.villa = ?
        """;

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, villaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("customer"),
                        rs.getInt("room_type"),
                        rs.getString("checkin_date"),
                        rs.getString("checkout_date"),
                        rs.getInt("price"),
                        rs.getObject("voucher") != null ? rs.getInt("voucher") : null,
                        rs.getInt("final_price")
                );

                booking.setPaymentStatus(rs.getString("payment_status"));
                booking.setHasCheckedin(rs.getBoolean("has_checkedin"));
                booking.setHasCheckedout(rs.getBoolean("has_checkedout"));
                booking.setId(rs.getInt("id")); // Jika kamu sudah tambahkan setId
                bookings.add(booking);
            }
        }
        return bookings;
    }

    // GET /villas?ci_date={checkin_date}&co_date={checkout_date} => Cari vila yang tersedia
    public List<Villas> searchAvailableVillas(String checkinDate, String checkoutDate) throws SQLException {
        List<Villas> available = new ArrayList<>();
        String sql = """
                SELECT DISTINCT v.*
                FROM villas v
                JOIN room_types r ON r.villa = v.id
                WHERE NOT EXISTS (
                    SELECT 1 FROM bookings b
                    WHERE b.room_type = r.id
                    AND NOT (b.checkout_date <= ? OR b.checkin_date >= ?)
                )
                """;

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, checkinDate);
            ps.setString(2, checkoutDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Villas villa = new Villas(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
                available.add(villa);
            }
        }
        return available;
    }

    public RoomTypes getVillaRoomById(int rid, int vid) throws SQLException, DataNotFoundException {
        String sql = "SELECT * FROM room_types WHERE id = ? AND villa = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rid);
            ps.setInt(2, vid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new RoomTypes(
                        rs.getInt("id"),
                        rs.getInt("villa"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("capacity"),
                        rs.getInt("price"),
                        rs.getString("bed_size"),
                        rs.getInt("has_desk") == 1,
                        rs.getInt("has_ac") == 1,
                        rs.getInt("has_tv") == 1,
                        rs.getInt("has_wifi") == 1,
                        rs.getInt("has_shower") == 1,
                        rs.getInt("has_hotwater") == 1,
                        rs.getInt("has_fridge") == 1
                );
            } else {
                return null;
            }
        }
    }

    // POST /villas => Menambahkan data vila
    public void createVilla(Villas villa) throws SQLException {
        String sql = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, villa.getName());
            ps.setString(2, villa.getDescription());
            ps.setString(3, villa.getAddress());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Gagal membuat villa");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    villa.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Gagal membuat villa, tidak mendapatkan id");
                }
            }
        }
    }

    // POST /villas/{id}/rooms => Menambahkan tipe kamar pada vila
    public void createVillasRooms(RoomTypes roomtypes) throws SQLException {
        String sql = """
                INSERT INTO room_types (villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv,
                has_wifi, has_shower, has_hotwater, has_fridge)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomtypes.getVilla_id());
            ps.setString(2, roomtypes.getName());
            ps.setInt(3, roomtypes.getQuantity());
            ps.setInt(4, roomtypes.getCapacity());
            ps.setInt(5, roomtypes.getPrice());
            ps.setString(6, roomtypes.getBed_size());
            ps.setBoolean(7, roomtypes.getHas_desk());
            ps.setBoolean(8, roomtypes.getHas_ac());
            ps.setBoolean(9, roomtypes.getHas_tv());
            ps.setBoolean(10, roomtypes.getHas_wifi());
            ps.setBoolean(11, roomtypes.getHas_shower());
            ps.setBoolean(12, roomtypes.getHas_hotwater());
            ps.setBoolean(13, roomtypes.getHas_fridge());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Gagal membuat ruangan");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    roomtypes.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Gagal membuat ruangan, tidak mendapatkan id");
                }
            }
        }
    }

    //PUT /villas{id} => Mengubah data suatu villa
    public void updateVilla(Villas newVilla) throws SQLException {
        String sql = """
                UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newVilla.getName());
            ps.setString(2, newVilla.getDescription());
            ps.setString(3, newVilla.getAddress());
            ps.setInt(4, newVilla.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to update villa");
            }

            System.out.println("Villa dengan id: " + newVilla.getId() + " berhasil di update");
        }
    }

    //PUT /villas/{id}/rooms/{id} => Mengubah informasi kamar suatu villa
    public void updateVillasRoomTypes(RoomTypes roomtypes) throws SQLException {
        String sql = """
                        UPDATE room_types SET
                            name = ?,
                            quantity = ?,
                            capacity = ?,
                            price = ?,
                            bed_size = ?,
                            has_desk = ?,
                            has_ac = ?,
                            has_tv = ?,
                            has_wifi = ?,
                            has_shower = ?,
                            has_hotwater = ?,
                            has_fridge = ?
                        WHERE id = ? AND villa = ?
                """;

        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomtypes.getName());
            ps.setInt(2, roomtypes.getQuantity());
            ps.setInt(3, roomtypes.getCapacity());
            ps.setInt(4, roomtypes.getPrice());
            ps.setString(5, roomtypes.getBed_size());
            ps.setBoolean(6, roomtypes.getHas_desk());
            ps.setBoolean(7, roomtypes.getHas_ac());
            ps.setBoolean(8, roomtypes.getHas_tv());
            ps.setBoolean(9, roomtypes.getHas_wifi());
            ps.setBoolean(10, roomtypes.getHas_shower());
            ps.setBoolean(11, roomtypes.getHas_hotwater());
            ps.setBoolean(12, roomtypes.getHas_fridge());
            ps.setInt(13, roomtypes.getId());
            ps.setInt(14, roomtypes.getVilla_id());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to update room type villa");
            }
        }
    }

    //DELETE /villas/rooms/{id} ==> Menghapus kamar suatu villa
    public void deleteVillaRoomTypes(int rid, int vid) throws SQLException {
        String sql = "DELETE from room_types WHERE id = ? AND villa = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rid);
            ps.setInt(2, vid);
            ps.executeUpdate();
        }
    }

    //DELETE /villas/{id} => Menghapus data suatu villa
    public void deleteVilla(int id) throws SQLException {
        String sql = "DELETE FROM villas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseBuilder.DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}