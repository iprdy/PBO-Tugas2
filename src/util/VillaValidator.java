package util;

import exceptions.BadRequestException;
import models.RoomTypes;
import models.Villas;

public class VillaValidator {
    public static void validatePostVilla (Villas villa) {
        if (villa.getId() != null) {
            throw new BadRequestException("field 'id' tidak boleh dikirim (akan dibuat otomatis)");
        }

        if (villa.getName() == null || villa.getName().isEmpty()) {
            throw new BadRequestException("Nama villa tidak boleh kosong");
        }

        if (villa.getDescription() == null || villa.getDescription().isEmpty()) {
            throw new BadRequestException("Deskripsi villa tidak boleh kosong");
        }

        if (villa.getAddress() == null || villa.getAddress().isEmpty()) {
            throw new BadRequestException("Alamat villa tidak boleh kosong");
        }
    }
}
