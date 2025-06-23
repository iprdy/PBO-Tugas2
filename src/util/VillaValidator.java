package util;

import exceptions.BadRequestException;
import models.RoomTypes;
import models.Villas;

public class VillaValidator {
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

        if (!(rt.getBed_size().equals("Double") || rt.getBed_size().equals("Queen") || rt.getBed_size().equals("King"))) {
            throw new BadRequestException("Ukuran kasur harus di antara: Double, Queen, atau King");
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
