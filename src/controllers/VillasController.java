package controllers;

import com.fasterxml.jackson.databind.ObjectMapper; // untuk JSON serialization
import com.sun.net.httpserver.HttpExchange;         // untuk handling HTTP request
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Review;
import models.RoomTypes;
import models.Booking;
import models.Villas;
import api.Response;
import java.sql.*;

public class VillasController {
    // GET /villas => Menampilkan semua data vila
    public List<Villas> getAllVillas() throws SQLException {
        List<Villas> villas = new ArrayList<>();
        String sql = "SELECT * FROM villas";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Connected to the database");

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
        return villas;
    }

    // GET /villas/{id} => Menampilkan detail data satu vila
    public Villas getVillaById(int id) throws SQLException {
        String sql = "SELECT * FROM villas WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Connected to the database");

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

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Connected to the database");

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

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Connected to the database");
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


    // GET /villas/{id}/reviews => Menampilkan semua review pada suatu vila
    public List<Review> getReviewsByVillaId(int villaId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = """
            SELECT rv.booking, rv.star, rv.title, rv.content
            FROM reviews rv
            JOIN bookings b ON rv.booking = b.id
            JOIN room_types rt ON b.room_type = rt.id
            WHERE rt.villa = ?
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Connected to the database");
            ps.setInt(1, villaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                );

                reviews.add(review);
            }
        }
        return reviews;
    }

    // GET /villas?ci_date={checkin_date}&co_date={checkout_date} => Cari vila yang tersedia
    public List<Villas> searchAvailableVillas(String checkinDate, String checkoutDate) throws SQLException {
        List<Villas> available = new ArrayList<>();
        String sql = """
            SELECT DISTINCT v.* FROM villas v
            JOIN room_types rt ON v.id = rt.villa
            WHERE rt.id NOT IN (
                SELECT b.room_type FROM bookings b
                WHERE NOT (
                    b.checkout_date <= ? OR b.checkin_date >= ?
                )
            )
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Connected to the database (availability)");
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
    
    // POST /villas => Menambahkan data vila
    public Villas createVilla(Villas villa) throws SQLException {
        String sql = "INSERT INTO villas (id, name, description, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Connected to the database");

            ps.setInt(1, villa.getId());
            ps.setString(2, villa.getName());
            ps.setString(3, villa.getDescription());
            ps.setString(4, villa.getAddress());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create villa");
            }

            System.out.println("Berhasil membuat villa");

            return villa;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            throw e;
        }
    }
    
    // POST /villas/{id}/rooms => Menambahkan tipe kamar pada vila
    public RoomTypes createVillasRooms(RoomTypes roomtypes) throws SQLException {
        String sql = """
                INSERT INTO room_types (id, villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv,
                has_wifi, has_shower, has_hotwater, has_fridge)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Connected to the database");

            ps.setInt(1, roomtypes.getId());
            ps.setInt(2, roomtypes.getVilla_id());
            ps.setString(3, roomtypes.getName());
            ps.setInt(4, roomtypes.getQuantity());
            ps.setInt(5, roomtypes.getCapacity());
            ps.setInt(6, roomtypes.getPrice());
            ps.setString(7, roomtypes.getBed_size());
            ps.setBoolean(8, roomtypes.isHas_desk());
            ps.setBoolean(9, roomtypes.isHas_ac());
            ps.setBoolean(10, roomtypes.isHas_tv());
            ps.setBoolean(11, roomtypes.isHas_wifi());
            ps.setBoolean(12, roomtypes.isHas_shower());
            ps.setBoolean(13, roomtypes.isHas_hotwater());
            ps.setBoolean(14, roomtypes.isHas_fridge());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create room type");
            }

            System.out.println("Berhasil membuat roomtype untuk villa");

            return roomtypes;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            throw e;
        }
    }

    //PUT /villas{id} => Mengubah data suatu villa
    public Villas updateVilla(Villas newVilla) throws SQLException {
        String sql = """
                UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

            ps.setString(1, newVilla.getName());
            ps.setString(2, newVilla.getDescription());
            ps.setString(3, newVilla.getAddress());
            ps.setInt(4, newVilla.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to update villa");
            }

            System.out.println("Villa dengan id: " + newVilla.getId() + " berhasil di update");

            return newVilla;
        }
    }

    //PUT /villas/{id}/rooms/{id} => Mengubah informasi kamar suatu villa
    public RoomTypes updateVillasRoomTypes(RoomTypes roomtypes) throws SQLException {
        String sql = "INSERT INTO room_types (villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");
            ps.setInt(1, roomtypes.getVilla_id());
            ps.setString(2, roomtypes.getName());
            ps.setInt(3, roomtypes.getQuantity());
            ps.setInt(4, roomtypes.getCapacity());
            ps.setInt(5, roomtypes.getPrice());
            ps.setString(6, roomtypes.getBed_size());
            ps.setBoolean(7, roomtypes.isHas_desk());
            ps.setBoolean(8, roomtypes.isHas_ac());
            ps.setBoolean(9, roomtypes.isHas_tv());
            ps.setBoolean(10, roomtypes.isHas_wifi());
            ps.setBoolean(11, roomtypes.isHas_shower());
            ps.setBoolean(12, roomtypes.isHas_hotwater());
            ps.setBoolean(13, roomtypes.isHas_fridge());
            ps.setInt(14, roomtypes.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to update room type villa");
            }

            System.out.println("Tipe ruangan pada villa " + roomtypes.getVilla_id() + " berhasil di update");

            return roomtypes;
        }
    }

    //DELETE /villas/rooms/{id}
    public void deleteVillaRoomTypes(int id) throws SQLException {
        String sql = "DELETE from room_types WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:../sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Berhasil menghapus tipe ruangan");
        }
    }

    //DELETE /villas/{id} => Menghapus data suatu villa
    public void deleteVilla(int id) throws SQLException {
        String sql = "DELETE FROM villas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Berhasil menghapus villa");
        }
    }

//    public void getBookingsByVillaId(HttpExchange exchange, int villaId) {
//        try {
//            PreparedStatement stmt = conn.prepareStatement(
//                    "SELECT b.* FROM bookings b " +
//                            "JOIN room_types r ON b.room_type = r.id " +
//                            "WHERE r.villa = ?"
//            );
//            stmt.setInt(1, villaId);
//
//            ResultSet rs = stmt.executeQuery();
//            List<Map<String, Object>> bookings = new ArrayList<>();
//            while (rs.next()) {
//                Map<String, Object> item = new HashMap<>();
//                item.put("id", rs.getInt("id"));
//                item.put("customer", rs.getInt("customer"));
//                item.put("room_type", rs.getInt("room_type"));
//                item.put("checkin_date", rs.getString("checkin_date"));
//                item.put("checkout_date", rs.getString("checkout_date"));
//                item.put("price", rs.getInt("price"));
//                item.put("voucher", rs.getObject("voucher"));
//                item.put("final_price", rs.getInt("final_price"));
//                item.put("payment_status", rs.getString("payment_status"));
//                item.put("has_checkedin", rs.getInt("has_checkedin"));
//                item.put("has_checkedout", rs.getInt("has_checkedout"));
//                bookings.add(item);
//            }
//
//            ObjectMapper mapper = new ObjectMapper();
//            String response = mapper.writeValueAsString(bookings);
//            Response res = new Response(exchange);
//            res.setBody(response);
//            res.send(200);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Response res = new Response(exchange);
//            res.setBody("{\"error\": \"" + e.getMessage() + "\"}");
//            res.send(500);
//        }
//    }
}
