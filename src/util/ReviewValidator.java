package util;

import exceptions.BadRequestException;
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

}
