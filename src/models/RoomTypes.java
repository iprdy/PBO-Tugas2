package models;

import exceptions.BadRequestException;

public class RoomTypes {
    private Integer id;
    private Integer villa_id;
    private String name;
    private Integer quantity;
    private Integer capacity;
    private Integer price;
    private String bed_size;
    private Boolean has_desk;
    private Boolean has_ac;
    private Boolean has_tv;
    private Boolean has_wifi;
    private Boolean has_shower;
    private Boolean has_hotwater;
    private Boolean has_fridge;

    public RoomTypes() {}

    public RoomTypes(int id, int villa_id, String name, int quantity, int capacity, int price, String bed_size, boolean has_desk, boolean has_ac, boolean has_tv, boolean has_wifi, boolean has_shower, boolean has_hotwater, boolean has_fridge) {
        this.id = id;
        this.villa_id = villa_id;
        this.name = name;
        this.quantity = quantity;
        this.capacity = capacity;
        this.price = price;
        this.bed_size = bed_size;
        this.has_desk = has_desk;
        this.has_ac = has_ac;
        this.has_tv = has_tv;
        this.has_wifi = has_wifi;
        this.has_shower = has_shower;
        this.has_hotwater = has_hotwater;
        this.has_fridge = has_fridge;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVilla_id() {
        return villa_id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getPrice() {
        return price;
    }

    public String getBed_size() {
        return bed_size;
    }

    public Boolean getHas_desk() {
        return has_desk;
    }

    public Boolean getHas_ac() {
        return has_ac;
    }

    public Boolean getHas_tv() {
        return has_tv;
    }

    public Boolean getHas_wifi() {
        return has_wifi;
    }

    public Boolean getHas_shower() {
        return has_shower;
    }

    public Boolean getHas_hotwater() {
        return has_hotwater;
    }

    public Boolean getHas_fridge() {
        return has_fridge;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVilla_id(Integer villa_id) {
        this.villa_id = villa_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setBed_size(String bed_size) {
        if (bed_size != null) {
            String lower = bed_size.trim().toLowerCase();
            switch (lower) {
                case "double" -> this.bed_size = "Double";
                case "queen" -> this.bed_size = "Queen";
                case "king" -> this.bed_size = "King";
                default -> this.bed_size = bed_size;
            }
        } else {
            this.bed_size = null;
        }
    }

    public void setHas_desk(Boolean has_desk) {
        this.has_desk = has_desk;
    }

    public void setHas_ac(Boolean has_ac) {
        this.has_ac = has_ac;
    }

    public void setHas_tv(Boolean has_tv) {
        this.has_tv = has_tv;
    }

    public void setHas_wifi(Boolean has_wifi) {
        this.has_wifi = has_wifi;
    }

    public void setHas_shower(Boolean has_shower) {
        this.has_shower = has_shower;
    }

    public void setHas_hotwater(Boolean has_hotwater) {
        this.has_hotwater = has_hotwater;
    }

    public void setHas_fridge(Boolean has_fridge) {
        this.has_fridge = has_fridge;
    }

    public void setIdAndVillaId(Integer id, Integer villa_id) {
        this.id = id;
        this.villa_id = villa_id;
    }
}
