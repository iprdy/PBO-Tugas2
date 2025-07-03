package util;

import api.Request;
import exceptions.BadRequestException;
import exceptions.DataNotFoundException;
import models.RoomTypes;
import models.Villas;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class VillaValidator {
    public static void checkVillaIdAndRoomTypeId(int rid, int vid) throws SQLException, DataNotFoundException {
        String sql = "SELECT * FROM room_types WHERE id = ? AND villa = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rid);
            ps.setInt(2, vid);
            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                throw new DataNotFoundException("ID Villa atau Room Type tidak ditemukan");
            }
        }
    }

    public static Map<String, String> validateQuery (Request req) {
        Map<String, String> rawQuery = req.getQueryParams();

        if (rawQuery.isEmpty()) {
            return rawQuery;
        }

        if (!rawQuery.containsKey("ci_date") || !rawQuery.containsKey("co_date")) {
            throw new BadRequestException("Wajib mengisi key 'ci_date' dan 'co_date'");
        }

        String ciDate = rawQuery.get("ci_date");
        String coDate = rawQuery.get("co_date");

        if (ciDate == null || ciDate.isEmpty() || coDate == null || coDate.isEmpty()) {
            throw new BadRequestException("Wajib mengisi value 'ci_date' dan 'co_date'");
        }

        LocalDate ci_dateParse;
        LocalDate co_dateParse;
        try {
            ci_dateParse = LocalDate.parse(ciDate);
            co_dateParse = LocalDate.parse(coDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Format tanggal harus 'YYYY-MM-DD'");
        }

        if (co_dateParse.isBefore(ci_dateParse)) {
            throw new BadRequestException("Tanggal pada co_date tidak boleh lebih awal dari ci_date");
        }

        Map<String, String> validatedQuery = new HashMap<>();
        validatedQuery.put("ci_date",ci_dateParse + " 00:00:00");
        validatedQuery.put("co_date", co_dateParse + " 23:59:59");

        return validatedQuery;
    }

    public static void validatePostVilla (Villas villa) {
        if (villa.getId() != null) {
            throw new BadRequestException("field 'id' tidak boleh dikirim (akan dibuat otomatis)");
        }

        if (villa.getName() == null || villa.getName().isBlank()) {
            throw new BadRequestException("Nama villa tidak boleh kosong");
        }

        if (villa.getDescription() == null || villa.getDescription().isBlank()) {
            throw new BadRequestException("Deskripsi villa tidak boleh kosong");
        }

        if (villa.getAddress() == null || villa.getAddress().isBlank()) {
            throw new BadRequestException("Alamat villa tidak boleh kosong");
        }
    }

    public static void validatePostVillaRooms (RoomTypes rt) {
        if (rt.getId() != null) {
            throw new BadRequestException("field 'id' tidak boleh dikirim (akan dibuat otomatis)");
        }

        if (rt.getVilla_id() != null) {
            throw new BadRequestException("field 'villa' tidak boleh dikirim (akan diambil dari path)");
        }

        if (rt.getName() == null || rt.getName().isBlank()) {
            throw new BadRequestException("Nama ruangan tidak boleh kosong");
        }

        if (rt.getQuantity() == null) {
            throw new BadRequestException("Kuantitas ruangan tidak boleh kosong");
        }

        if (rt.getQuantity() <= 0) {
            throw new BadRequestException("Kuantitas ruangan harus lebih dari 0");
        }

        if (rt.getCapacity() == null) {
            throw new BadRequestException("Kapasitas ruangan tidak boleh kosong");
        }

        if (rt.getCapacity() <= 0) {
            throw new BadRequestException("Kapasitas ruangan harus lebih dari 0");
        }

        if (rt.getPrice() == null) {
            throw new BadRequestException("Harga tidak boleh kosong");
        }

        if (rt.getPrice() <= 0) {
            throw new BadRequestException("Harga harus lebih dari 0");
        }

        if (rt.getBed_size() == null || rt.getBed_size().isBlank()) {
            throw new BadRequestException("Bed size tidak boleh kosong");
        }

        if (!(rt.getBed_size().equals("double") || rt.getBed_size().equals("queen") || rt.getBed_size().equals("king"))) {
            throw new BadRequestException("Ukuran kasur harus di antara: double, queen, atau king");
        }

        if (rt.getHas_desk() == null) {
            throw new BadRequestException("Informasi meja tidak boleh kosong");
        }

        if (rt.getHas_ac() == null) {
            throw new BadRequestException("Informasi AC tidak boleh kosong");
        }

        if (rt.getHas_tv() == null) {
            throw new BadRequestException("Informasi TV tidak boleh kosong");
        }

        if (rt.getHas_wifi() == null) {
            throw new BadRequestException("Informasi WiFi tidak boleh kosong");
        }

        if (rt.getHas_shower() == null) {
            throw new BadRequestException("Informasi shower tidak boleh kosong");
        }

        if (rt.getHas_hotwater() == null) {
            throw new BadRequestException("Informasi air panas tidak boleh kosong");
        }

        if (rt.getHas_fridge() == null) {
            throw new BadRequestException("Informasi kulkas tidak boleh kosong");
        }
    }
}
