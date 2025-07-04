package util;

import exceptions.BadRequestException;
import models.Voucher;

public class VoucherValidator {
    public static void validatePostVoucher(Voucher voucher) {
        // Tidak boleh kirim ID (akan dibuat otomatis)
        if (voucher.getId() != 0) {
            throw new BadRequestException("Field 'id' tidak boleh dikirim (akan dibuat otomatis)");
        }

        if (voucher.getCode() == null || voucher.getCode().isBlank()) {
            throw new BadRequestException("Kode voucher tidak boleh kosong");
        }

        if (voucher.getDescription() == null || voucher.getDescription().isBlank()) {
            throw new BadRequestException("Deskripsi voucher tidak boleh kosong");
        }

        if (voucher.getDiscount() <= 0 || voucher.getDiscount() > 100) {
            throw new BadRequestException("Diskon harus di antara 0 - 100");
        }

        if (voucher.getStartDate() == null || voucher.getStartDate().isBlank()) {
            throw new BadRequestException("Tanggal mulai tidak boleh kosong");
        }

        if (voucher.getEndDate() == null || voucher.getEndDate().isBlank()) {
            throw new BadRequestException("Tanggal berakhir tidak boleh kosong");
        }

        if (voucher.getEndDate().compareTo(voucher.getStartDate()) < 0) {
            throw new BadRequestException("Tanggal akhir tidak boleh lebih awal dari tanggal mulai");
        }
    }
}
