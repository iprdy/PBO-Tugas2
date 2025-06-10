package models;

public class Booking {
    private int id;
    private String guest;
    private String villa;
    private String date;

    public Booking(int id, String guest, String villa, String date) {
        this.id = id;
        this.guest = guest;
        this.villa = villa;
        this.date = date;
    }

    public Booking(String guest, String villa, String date) {
        this.guest = guest;
        this.villa = villa;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getGuest() {
        return guest;
    }

    public String getVilla() {
        return villa;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public void setVilla(String villa) {
        this.villa = villa;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
