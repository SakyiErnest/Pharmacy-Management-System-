import java.util.*;

public class PharmacyManagementSystem {
    private HashMap<String, Drug> drugMap;
    private HashMap<String, Supplier> supplierMap;
    private HashMap<String, Customer> customerMap;
    private ArrayList<Sale> sales;
    private HashMap<String, LinkedList<Purchase>> purchaseHistory;

    public PharmacyManagementSystem() {
        drugMap = new HashMap<>();
        supplierMap = new HashMap<>();
        customerMap = new HashMap<>();
        sales = new ArrayList<>();
        purchaseHistory = new HashMap<>();
    }

    // Add Drug
    public void addDrug(Drug drug) {
        if (!drugMap.containsKey(drug.getDrugCode())) {
            drugMap.put(drug.getDrugCode(), drug);
            System.out.println("Drug added successfully: " + drug);
        } else {
            System.out.println("Drug with code " + drug.getDrugCode() + " already exists.");
        }
    }

    // Remove Drug
    public void removeDrug(String drugCode) {
        if (drugMap.containsKey(drugCode)) {
            drugMap.remove(drugCode);
            System.out.println("Drug removed successfully.");
        } else {
            System.out.println("Drug with code " + drugCode + " not found.");
        }
    }

    // Search Drug
    public Drug searchDrug(String drugCode) {
        return drugMap.get(drugCode);
    }

    // Get All Drugs
    public Collection<Drug> getAllDrugs() {
        return drugMap.values();
    }

    // Add Purchase
    public void addPurchase(Purchase purchase) {
        purchaseHistory.computeIfAbsent(purchase.getDrugCode(), k -> new LinkedList<>()).add(purchase);
    }

    // View Purchase History
    public void viewPurchaseHistory(String drugCode) {
        LinkedList<Purchase> purchases = purchaseHistory.get(drugCode);
        if (purchases != null) {
            for (Purchase purchase : purchases) {
                System.out.println(purchase);
            }
        } else {
            System.out.println("No purchase history for drug code: " + drugCode);
        }
    }

    // Add Sale
    public void addSale(Sale sale) {
        sales.add(sale);
        Drug drug = drugMap.get(sale.getDrugCode());
        if (drug != null) {
            drug.setQuantity(drug.getQuantity() - 1); // Decrease the quantity by 1 for each sale
        }
        System.out.println("Sale added: " + sale);
    }

    // View Sales
    public void viewSales() {
        for (Sale sale : sales) {
            System.out.println(sale);
        }
    }

    // Add Supplier
    public void addSupplier(Supplier supplier) {
        supplierMap.put(supplier.getSupplierID(), supplier);
    }

    // Search Supplier
    public Supplier searchSupplier(String supplierID) {
        return supplierMap.get(supplierID);
    }

    // Add Customer
    public void addCustomer(Customer customer) {
        customerMap.put(customer.getCustomerID(), customer);
    }

    // Search Customer
    public Customer searchCustomer(String customerID) {
        return customerMap.get(customerID);
    }
}
