import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pharmacy";
    private static final String USER = "pharmacy_user";
    private static final String PASSWORD = "ernie123";

    private Connection connection;

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Drug operations
    public void addDrugToDatabase(Drug drug) throws SQLException {
        String sql = "INSERT INTO drugs (drugCode, name, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, drug.getDrugCode());
            stmt.setString(2, drug.getName());
            stmt.setInt(3, drug.getQuantity());
            stmt.setDouble(4, drug.getPrice());
            stmt.executeUpdate();
        }
    }

    public void updateDrugInDatabase(Drug drug) throws SQLException {
        String sql = "UPDATE drugs SET name = ?, quantity = ?, price = ? WHERE drugCode = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, drug.getName());
            stmt.setInt(2, drug.getQuantity());
            stmt.setDouble(3, drug.getPrice());
            stmt.setString(4, drug.getDrugCode());
            stmt.executeUpdate();
        }
    }

    public void removeDrugFromDatabase(String drugCode) throws SQLException {
        String sql = "DELETE FROM drugs WHERE drugCode = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, drugCode);
            stmt.executeUpdate();
        }
    }

    public List<Drug> retrieveAllDrugsFromDatabase() throws SQLException {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT * FROM drugs";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String drugCode = rs.getString("drugCode");
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                Drug drug = new Drug(drugCode, name, quantity, price);
                drugs.add(drug);
            }
        }
        return drugs;
    }

    public Drug searchDrugInDatabase(String drugCode) throws SQLException {
        String sql = "SELECT * FROM drugs WHERE drugCode = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, drugCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    return new Drug(drugCode, name, quantity, price);
                }
            }
        }
        return null;
    }

    public List<Drug> searchDrugsInDatabase(String searchTerm) throws SQLException {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT * FROM drugs WHERE drugCode LIKE ? OR name LIKE ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            String wildcardTerm = "%" + searchTerm + "%";
            stmt.setString(1, wildcardTerm);
            stmt.setString(2, wildcardTerm);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String drugCode = rs.getString("drugCode");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    Drug drug = new Drug(drugCode, name, quantity, price);
                    drugs.add(drug);
                }
            }
        }
        return drugs;
    }

    // Sale operations
    public void addSaleToDatabase(Sale sale) throws SQLException {
        String sql = "INSERT INTO sales (saleID, drugCode, quantity, totalPrice, dateTime) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, sale.getSaleID());
            stmt.setString(2, sale.getDrugCode());
            stmt.setInt(3, sale.getQuantity());
            stmt.setDouble(4, sale.getTotalPrice());
            stmt.setTimestamp(5, Timestamp.valueOf(sale.getDateTime()));
            stmt.executeUpdate();
        }
    }


    public List<Sale> retrieveSalesHistory(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE dateTime BETWEEN ? AND ? ORDER BY dateTime DESC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String saleID = rs.getString("saleID");
                    String drugCode = rs.getString("drugCode");
                    int quantity = rs.getInt("quantity");
                    double totalPrice = rs.getDouble("totalPrice");
                    LocalDateTime dateTime = rs.getTimestamp("dateTime").toLocalDateTime();
                    Sale sale = new Sale(saleID, drugCode, quantity, totalPrice, dateTime);
                    sales.add(sale);
                }
            }
        }
        return sales;
    }

    public void updateDrugQuantityInDatabase(String drugCode, int newQuantity) throws SQLException {
        String sql = "UPDATE drugs SET quantity = ? WHERE drugCode = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, drugCode);
            stmt.executeUpdate();
        }
    }
}