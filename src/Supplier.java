public class Supplier {
    private String supplierID;
    private String name;
    private String location;

    // Constructor
    public Supplier(String supplierID, String name, String location) {
        this.supplierID = supplierID;
        this.name = name;
        this.location = location;
    }

    // Getters and Setters
    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierID='" + supplierID + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
