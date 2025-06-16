package models;

public class Booking {
    private int id;
    private int customer;
    private int roomType;
    private String checkinDate;
    private String checkoutDate;
    private int price;
    private Integer voucher; // Boleh null
    private int finalPrice;
    private String paymentStatus;
    private boolean hasCheckedin;
    private boolean hasCheckedout;

    public Booking() {
        // Default constructor for Jackson
    }

    public Booking(int customer, int roomType, String checkinDate, String checkoutDate,
                   int price, Integer voucher, int finalPrice) {
        this.customer = customer;
        this.roomType = roomType;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.price = price;
        this.voucher = voucher;
        this.finalPrice = finalPrice;
        this.paymentStatus = "waiting";
        this.hasCheckedin = false;
        this.hasCheckedout = false;
    }

    // Getters
    public int getCustomer() { return customer; }
    public int getRoomType() { return roomType; }
    public String getCheckinDate() { return checkinDate; }
    public String getCheckoutDate() { return checkoutDate; }
    public int getPrice() { return price; }
    public Integer getVoucher() { return voucher; }
    public int getFinalPrice() { return finalPrice; }
    public String getPaymentStatus() { return paymentStatus; }
    public boolean isHasCheckedin() { return hasCheckedin; }
    public boolean isHasCheckedout() { return hasCheckedout; }
    public int getId() { return id; }


    // Setters
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setHasCheckedin(boolean hasCheckedin) { this.hasCheckedin = hasCheckedin; }
    public void setHasCheckedout(boolean hasCheckedout) { this.hasCheckedout = hasCheckedout; }
    public void setId(int id) { this.id = id;}
}
