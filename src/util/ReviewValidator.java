package util;

import exceptions.BadRequestException;
import exceptions.DataNotFoundException;
import java.sql.*;
import models.Review;

public class ReviewValidator {
    public static void validatePostReview(Review review) {
        if (review.getStar() < 1 || review.getStar() > 5) {
            throw new BadRequestException("Rating bintang harus antara 1 sampai 5");
        }

        if (review.getTitle() == null || review.getTitle().isBlank()) {
            throw new BadRequestException("Judul ulasan tidak boleh kosong");
        }

        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new BadRequestException("Isi ulasan tidak boleh kosong");
        }
    }

    public static void checkBookingBelongsToCustomer(int bookingId, int customerId) throws SQLException, DataNotFoundException {
        String sql = "SELECT * FROM bookings WHERE id = ? AND customer = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:villa_booking.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, customerId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new DataNotFoundException("Pemesanan tidak ditemukan atau bukan milik pelanggan tersebut.");
            }
        }
    }
}