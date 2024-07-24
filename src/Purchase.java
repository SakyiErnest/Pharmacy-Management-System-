import java.time.LocalDateTime;

public class Purchase {
    private String purchaseID;
    private String drugCode;
    private LocalDateTime dateTime;
    private Customer buyer;
    private double totalAmount;

    // Constructor
    public Purchase(String purchaseID, String drugCode, LocalDateTime dateTime, Customer buyer, double totalAmount) {
        this.purchaseID = purchaseID;
        this.drugCode = drugCode;
        this.dateTime = dateTime;
        this.buyer = buyer;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(String purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public void setBuyer(Customer buyer) {
        this.buyer = buyer;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseID='" + purchaseID + '\'' +
                ", drugCode='" + drugCode + '\'' +
                ", dateTime=" + dateTime +
                ", buyer=" + buyer +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
