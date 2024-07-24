import java.time.LocalDateTime;

public class Sale {
    private String saleID;
    private String drugCode;
    private int quantity;
    private double totalPrice;
    private LocalDateTime dateTime;

    public Sale(String saleID, String drugCode, int quantity, double totalPrice, LocalDateTime dateTime) {
        this.saleID = saleID;
        this.drugCode = drugCode;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
    }

    // Getters
    public String getSaleID() { return saleID; }
    public String getDrugCode() { return drugCode; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getDateTime() { return dateTime; }

    // Setters
    public void setSaleID(String saleID) { this.saleID = saleID; }
    public void setDrugCode(String drugCode) { this.drugCode = drugCode; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    @Override
    public String toString() {
        return "Sale{" +
                "saleID='" + saleID + '\'' +
                ", drugCode='" + drugCode + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", dateTime=" + dateTime +
                '}';
    }
}