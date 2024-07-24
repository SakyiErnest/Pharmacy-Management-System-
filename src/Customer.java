public class Customer {
    private String customerID;
    private String name;
    private String contactInformation;

    // Constructor
    public Customer(String customerID, String name, String contactInformation) {
        this.customerID = customerID;
        this.name = name;
        this.contactInformation = contactInformation;
    }

    // Getters and Setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", name='" + name + '\'' +
                ", contactInformation='" + contactInformation + '\'' +
                '}';
    }
}
