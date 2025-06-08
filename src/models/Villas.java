package models;

public class Villas {
    private int id;
    private String name;
    private String description;
    private String address;

    public Villas(int id, String name, String description, String address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void getName() {
        return name;
    }

    public void getDescription() {
        return description;
    }

    public void getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}