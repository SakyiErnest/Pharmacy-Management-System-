import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import javafx.stage.FileChooser;

public class PharmacyManagementSystemApp extends Application {
    private PharmacyManagementSystem pms;
    private DatabaseConnection dbConnection;
    private ObservableList<Drug> drugList;
    private TableView<Drug> drugTable;
    private TableView<Sale> salesTable;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    @Override
    public void start(Stage primaryStage) {
        pms = new PharmacyManagementSystem();
        dbConnection = new DatabaseConnection();
        drugList = FXCollections.observableArrayList();

        primaryStage.setTitle("Pharmacy Management System");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createTab("Drug Management", createDrugManagementPane()),
                createTab("Sales", createSalesPane()),
                createTab("Sales History", createSalesHistoryPane())
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(createHeader());
        mainLayout.setCenter(tabPane);

        Scene scene = new Scene(mainLayout, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        loadDrugsFromDatabase();
    }

    private Node createHeader() {
        Label titleLabel = new Label("Pharmacy Management System");
        titleLabel.getStyleClass().add("header-title");
        HBox header = new HBox(titleLabel);
        header.getStyleClass().add("header");
        return header;
    }

    private Tab createTab(String title, Node content) {
        Tab tab = new Tab(title);
        tab.setContent(content);
        return tab;
    }

    private VBox createDrugManagementPane() {
        TextField drugCodeField = createStyledTextField("Enter Drug Code");
        TextField drugNameField = createStyledTextField("Enter Drug Name");
        TextField drugQuantityField = createStyledTextField("Enter Drug Quantity");
        TextField drugPriceField = createStyledTextField("Enter Drug Price");
        Button addButton = createStyledButton("Add Drug");
        Button removeButton = createStyledButton("Remove Selected Drug");
        Button viewButton = createStyledButton("View All Drugs");

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(drugCodeField, drugNameField, drugQuantityField, drugPriceField);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, removeButton, viewButton);

        TextField searchField = createStyledTextField("Search by Drug Code or Name");
        searchField.setMaxWidth(Double.MAX_VALUE);

        drugTable = new TableView<>();
        setupDrugTable();

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(inputBox, buttonBox, searchField, drugTable);

        setupSearchFunctionality(searchField);

        addButton.setOnAction(e -> addDrug(drugCodeField, drugNameField, drugQuantityField, drugPriceField));
        removeButton.setOnAction(e -> removeSelectedDrug());
        viewButton.setOnAction(e -> viewAllDrugs());

        return layout;
    }

    private VBox createSalesPane() {
        TextField drugCodeField = createStyledTextField("Enter Drug Code");
        TextField quantityField = createStyledTextField("Enter Quantity");
        Button sellButton = createStyledButton("Sell");
        Button printButton = createStyledButton("Print Sale");

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(drugCodeField, quantityField, sellButton, printButton);

        TableView<Sale> salesTable = new TableView<>();
        setupSalesTable(salesTable);

        sellButton.setOnAction(e -> makeSale(drugCodeField, quantityField, salesTable));
        printButton.setOnAction(e -> printSaleTransaction(salesTable.getSelectionModel().getSelectedItem()));

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(inputBox, salesTable);

        return layout;
    }




    private VBox createSalesHistoryPane() {
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        Button viewSalesButton = createStyledButton("View Sales");

        HBox datePickerBox = new HBox(10);
        datePickerBox.getChildren().addAll(
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker,
                viewSalesButton
        );

        salesTable = new TableView<>();
        setupSalesTable(salesTable);

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(datePickerBox, salesTable);

        viewSalesButton.setOnAction(e -> viewSalesHistory());

        return layout;
    }

    private void setupDrugTable() {
        TableColumn<Drug, String> codeCol = new TableColumn<>("Drug Code");
        TableColumn<Drug, String> nameCol = new TableColumn<>("Name");
        TableColumn<Drug, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Drug, Double> priceCol = new TableColumn<>("Price");

        codeCol.setCellValueFactory(new PropertyValueFactory<>("drugCode"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        drugTable.getColumns().addAll(codeCol, nameCol, quantityCol, priceCol);
        drugTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupSalesTable(TableView<Sale> table) {
        TableColumn<Sale, String> saleIdCol = new TableColumn<>("Sale ID");
        TableColumn<Sale, String> drugCodeCol = new TableColumn<>("Drug Code");
        TableColumn<Sale, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Sale, Double> totalPriceCol = new TableColumn<>("Total Price");
        TableColumn<Sale, LocalDateTime> dateTimeCol = new TableColumn<>("Date/Time");

        saleIdCol.setCellValueFactory(new PropertyValueFactory<>("saleID"));
        drugCodeCol.setCellValueFactory(new PropertyValueFactory<>("drugCode"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        table.getColumns().addAll(saleIdCol, drugCodeCol, quantityCol, totalPriceCol, dateTimeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.getStyleClass().add("styled-text-field");
        return textField;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        return button;
    }

    private void setupSearchFunctionality(TextField searchField) {
        FilteredList<Drug> filteredData = new FilteredList<>(drugList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(createPredicate(newValue));
        });

        SortedList<Drug> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(drugTable.comparatorProperty());
        drugTable.setItems(sortedData);
    }

    private Predicate<Drug> createPredicate(String searchText) {
        return drug -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lowerCaseFilter = searchText.toLowerCase();
            return drug.getDrugCode().toLowerCase().contains(lowerCaseFilter) ||
                    drug.getName().toLowerCase().contains(lowerCaseFilter);
        };
    }

    private void loadDrugsFromDatabase() {
        try {
            List<Drug> drugs = dbConnection.retrieveAllDrugsFromDatabase();
            drugList.setAll(drugs);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load drugs from database: " + e.getMessage());
        }
    }

    private void addDrug(TextField drugCodeField, TextField drugNameField, TextField drugQuantityField, TextField drugPriceField) {
        try {
            String drugCode = drugCodeField.getText();
            String drugName = drugNameField.getText();
            int quantity = Integer.parseInt(drugQuantityField.getText());
            double price = Double.parseDouble(drugPriceField.getText());

            Drug drug = new Drug(drugCode, drugName, quantity, price);
            dbConnection.addDrugToDatabase(drug);
            drugList.add(drug);

            clearTextFields(drugCodeField, drugNameField, drugQuantityField, drugPriceField);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please check quantity and price.");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add drug to database: " + e.getMessage());
        }
    }

    private void removeSelectedDrug() {
        Drug selectedDrug = drugTable.getSelectionModel().getSelectedItem();
        if (selectedDrug != null) {
            try {
                dbConnection.removeDrugFromDatabase(selectedDrug.getDrugCode());
                drugList.remove(selectedDrug);
            } catch (SQLException e) {
                showAlert("Error", "Failed to remove drug from database: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "Please select a drug to remove.");
        }
    }

    private void viewAllDrugs() {
        loadDrugsFromDatabase();
    }

    private void makeSale(TextField drugCodeField, TextField quantityField, TableView<Sale> salesTable) {
        try {
            String drugCode = drugCodeField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            Drug drug = dbConnection.searchDrugInDatabase(drugCode);
            if (drug == null) {
                showAlert("Error", "Drug not found");
                return;
            }

            if (drug.getQuantity() < quantity) {
                showAlert("Error", "Insufficient stock");
                return;
            }

            double totalPrice = drug.getPrice() * quantity;
            String saleID = generateSaleID();
            LocalDateTime now = LocalDateTime.now();

            Sale sale = new Sale(saleID, drugCode, quantity, totalPrice, now);
            dbConnection.addSaleToDatabase(sale);

            // Update drug quantity
            drug.setQuantity(drug.getQuantity() - quantity);
            dbConnection.updateDrugInDatabase(drug);

            // Update UI
            salesTable.getItems().add(sale);
            loadDrugsFromDatabase();

            drugCodeField.clear();
            quantityField.clear();
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid quantity");
        } catch (SQLException e) {
            showAlert("Error", "Failed to process sale: " + e.getMessage());
        }
    }

    private void viewSalesHistory() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Error", "Please select both start and end dates");
            return;
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        try {
            List<Sale> sales = dbConnection.retrieveSalesHistory(startDateTime, endDateTime);
            salesTable.getItems().setAll(sales);
        } catch (SQLException e) {
            showAlert("Error", "Failed to retrieve sales history: " + e.getMessage());
        }
    }

    private String generateSaleID() {
        return "SALE-" + System.currentTimeMillis();
    }

    private void clearTextFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        try {
            dbConnection.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    private void printSaleTransaction(Sale sale) {
        if (sale == null) {
            showAlert("Error", "Please select a sale to print.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Set initial file name to the saleID
        fileChooser.setInitialFileName(sale.getSaleID() + ".pdf");

        // Set initial directory to user's home directory
        fileChooser.setInitialDirectory(new File("D:\\Projects\\Receipts"));


        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PdfWriter writer = new PdfWriter(file);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Sale Transaction Receipt").setFontSize(18).setBold());
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("Sale ID: " + sale.getSaleID()));
                document.add(new Paragraph("Drug Code: " + sale.getDrugCode()));
                document.add(new Paragraph("Quantity: " + sale.getQuantity()));
                document.add(new Paragraph("Total Price: $" + String.format("%.2f", sale.getTotalPrice())));
                document.add(new Paragraph("Date/Time: " + sale.getDateTime()));

                document.close();

                showAlert("Success", "PDF created successfully!\nSaved to: " + file.getAbsolutePath());
            } catch (IOException ex) {
                showAlert("Error", "Failed to create PDF: " + ex.getMessage());
            }
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}