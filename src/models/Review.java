package models;

public class Review {
    private int bookingId;
    private int star;
    private String title;
    private String content;

    public Review(int bookingId, int star, String title, String content) {
        this.bookingId = bookingId;
        this.star = star;
        this.title = title;
        this.content = content;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getStar() {
        return star;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
