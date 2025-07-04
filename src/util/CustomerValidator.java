package util;

import exceptions.BadRequestException;
import models.Customer;

public class CustomerValidator {
    public static void validatePostCustomer(Customer customer) {
        if (customer.getId() != 0) {
            throw new BadRequestException("Field 'id' tidak boleh dikirim (akan dibuat otomatis)");
        }

        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new BadRequestException("Nama customer tidak boleh kosong");
        }

        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new BadRequestException("Email customer tidak boleh kosong");
        }

        if (!customer.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new BadRequestException("Format email tidak valid");
        }

        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new BadRequestException("Nomor telepon customer tidak boleh kosong");
        }

        if (!customer.getPhone().matches("^\\+?[0-9]{9,15}$")) {
            throw new BadRequestException("Format nomor telepon tidak valid");
        }
    }
}
