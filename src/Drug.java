import java.util.List;

public class Drug {
    private String drugCode;
    private String name;
    private int quantity;
    private double price;
    private List<Supplier> suppliers;

    // Constructor with suppliers
    public Drug(String drugCode, String name, int quantity, double price, List<Supplier> suppliers) {
        this.drugCode = drugCode;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.suppliers = suppliers;
    }

    // Constructor without suppliers
    public Drug(String drugCode, String name, int quantity, double price) {
        this(drugCode, name, quantity, price, null);
    }

    // Getters and Setters
    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "drugCode='" + drugCode + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", suppliers=" + suppliers +
                '}';
    }
}