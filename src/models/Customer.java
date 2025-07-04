package models;

import java.util.List;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;

    private List<Booking> bookings;


    // Constructor default (diperlukan saat parsing dari JSON / ResultSet tanpa parameter)
    public Customer() {}

    // Constructor lengkap
    public Customer(int id, String name, String email, String phone){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

}
