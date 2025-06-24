package models;

public class Review {
    private int booking;       // PK & FK
    private int star;
    private String title;
    private String content;

    public Review() {

    }

    // Constructor lengkap
    public Review(int booking, int star, String title, String content) {
        this.booking = booking;
        this.star = star;
        this.title = title;
        this.content = content;
    }

    // Getter & Setter
    public int getBooking() { return booking; }

    public int getStar() { return star; }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public void setBooking(int booking) { this.booking = booking; }

    public void setStar(int star) { this.star = star; }

    public void setTitle(String title) { this.title = title; }

    public void setContent(String content) { this.content = content; }
}
