package controllers;

import models.RoomTypes;
import models.Villas;

import java.sql.*;

public class VillasController {

    //POST /villas => menambahkan data vila
    public Villas createVilla(Villas villa) throws SQLException {
        String sql = "INSERT INTO villas (id, name, description, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

            ps.setInt(1, villa.getId());
            ps.setString(2, villa.getName());
            ps.setString(3, villa.getDescription());
            ps.setString(4, villa.getAddress());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create villa");
            }

            System.out.println("Berhasil menambahkan villa ke database");

            return villa;

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            throw e;
        }
    }

    //POST /villas/{id}/rooms => Menambahkan tipe kamar pada vila
    public RoomTypes createVillasRooms(RoomTypes roomtypes) throws SQLException {
        String sql = """
                INSERT INTO villasrooms (id, villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv,has_wifi,has_shower,has_hotwater,has_fridge) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

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
                throw new SQLException("Failed to create room type villa");
            }

            return roomtypes;

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            throw e;
        }
    }

    //PUT /villas{id} => Mengubah data suatu villa
    public Villas updateVilla(Villas villa) throws SQLException {
        //ambil villa nya dari method get villa

        String sql = """
                UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

            ps.setString(1, villa.getName());
            ps.setString(2, villa.getDescription());
            ps.setString(3, villa.getAddress());
            ps.setInt(4, villa.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to update villa");
            }

            return villa; //Ini harusnya get villa lagi biar dia ngereturn villa yang baru di update
        }
    }

    //PUT /villas/{id}/rooms/{id} => Mengubah informasi kamar suatu villa
    public RoomTypes updateVillasRoomTypes(RoomTypes roomtypes) throws SQLException {
        //ambil roomtype dari method get roomtype

        String sql = """
                UPDATE villas SET 
                villa = ?, 
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
                WHERE id = ? 
                """;

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

            return roomtypes; //Return data villa yang diupdate, nanti diganti
        }
    }

    //DELETE /villas/rooms/{id}
    public void deleteVillaRoomTypes(int id) throws SQLException {
        //Get villa untuk mengecek apakah villanya ada

        String sql = "DELETE from room_types WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    //DELETE /villas/{id} => Menghapus data suatu villa
    public void deleteVilla(int id) throws SQLException {
        //Get villa untuk mengecek apakah villa nya ada

        String sql = "DELETE FROM villas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
        PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Has connected to the database");

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
