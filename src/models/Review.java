package models;

public class Review {
    private int id;
    private int customerId;
    private int villaId;
    private int bookingId;
    private String comment;
    private int rating;

    public Review(int id, int customerId, int villaId, int bookingId, String comment, int rating) {
        this.id = id;
        this.customerId = customerId;
        this.villaId = villaId;
        this.bookingId = bookingId;
        this.comment = comment;
        this.rating = rating;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getVillaId() {
        return villaId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getComment() {
        return comment;
    }

    // Setters
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
