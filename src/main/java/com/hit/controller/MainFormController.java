package com.hit.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hit.dao.CustomerObjectDAO;
import com.hit.dao.PayObjectDAO;
import com.hit.dao.ProductObjectDAO;
import com.hit.driver.AppController;
import com.hit.model.CustomerObject;
import com.hit.model.PayObject;
import com.hit.model.ProductObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.*;

import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.lang.reflect.Type;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.hit.model.CustomerObject.*;

public class MainFormController implements Initializable {

    @FXML
    private Button dashboardBtn;

    @FXML
    private AnchorPane form_inventory;

    @FXML
    private Button inventoryBtn;

    @FXML
    private ComboBox<String> inventory_choose_status;

    @FXML
    private ComboBox<String> inventory_choose_type;

    @FXML
    private TableColumn<ProductObject, String> inventory_col_IDProduct;

    @FXML
    private TableColumn<ProductObject, String> inventory_col_Price;

    @FXML
    private TableColumn<ProductObject, String> inventory_col_ProductName;

    @FXML
    private TableColumn<ProductObject, String> inventory_col_Status;

    @FXML
    private TableColumn<ProductObject, String> inventory_col_Type;

    @FXML
    private TableColumn<ProductObject, String> inventory_col_stock;

    @FXML
    private ImageView inventory_image_view;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private TextField inventory_price;

    @FXML
    private TextField inventory_product_id;

    @FXML
    private TextField inventory_product_name;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TableView<ProductObject> inventory_table_view;

    @FXML
    private Button menuBtn;

    @FXML
    private Button signoutBtn;

    @FXML
    private TextField menu_amount;

    @FXML
    private TableColumn<?, Double> menu_col_price;

    @FXML
    private TableColumn<?, ?> menu_col_productName;

    @FXML
    private TableColumn<?, ?> menu_col_quantity;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private TableView<PayObject> menu_tableView;

    @FXML
    private Label menu_total;

    @FXML
    private Label menu_change;

    @FXML
    private AnchorPane form_menu;

    @FXML
    private AnchorPane form_dashboard;

    @FXML
    private AreaChart<String, Number> barChart;

    @FXML
    private Label dashboard_display_num_customers;

    @FXML
    private Label dashboard_display_totalIncome;

    private Alert alert;

    private String[] typeList = {"Meals", "Drinks"};

    private String[] statusList = {"Available", "Unavailable"};

    private static final String FILE_PATH = "productData.json";

    private List<ProductObject> productList = new ArrayList<>();

    private ObservableList<ProductObject> cardListData = FXCollections.observableArrayList();

    private ProductObjectDAO productObjectDAO = new ProductObjectDAO();

    private CustomerObjectDAO customerObjectDAO = new CustomerObjectDAO();

    private PayObjectDAO payObjectDAO = new PayObjectDAO();

    public void deleteProductFromJsonFile(ProductObject product) {
        ProductObjectDAO productObjectDAO = new ProductObjectDAO();
        productObjectDAO.deleteProduct(product);
    }

    public void deleteBtn() {
        ProductObject selectedProduct = inventory_table_view.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation Dialog");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete the product?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                inventory_table_view.getItems().remove(selectedProduct);
                deleteProductFromJsonFile(selectedProduct);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Product has been deleted!");
                alert.showAndWait();

                clearBtn();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("No product selected!");
            alert.showAndWait();
        }
    }


    public void updateBtn() {
        ProductObject selectedProduct = inventory_table_view.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            String newProductID = inventory_product_id.getText();
            if (!newProductID.equals(selectedProduct.getProduct_id())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Can't change product ID, instead delete the product");
                alert.showAndWait();
            } else {
                if (selectedProduct != null) {
                    String newProductName = inventory_product_name.getText();
                    String newType = inventory_choose_type.getSelectionModel().getSelectedItem().toString();
                    Integer newStock = Integer.parseInt(inventory_stock.getText());
                    Double newPrice = Double.parseDouble(inventory_price.getText());
                    String newStatus = inventory_choose_status.getSelectionModel().getSelectedItem().toString();
                    String newProductImage = inventory_image_view.getImage().getUrl();


                    selectedProduct.setProduct_name(newProductName);
                    selectedProduct.setType(newType);
                    selectedProduct.setStock(newStock);
                    selectedProduct.setPrice(newPrice);
                    selectedProduct.setStatus(newStatus);
                    selectedProduct.setProduct_image(newProductImage);

                    updateProductInJsonFile(selectedProduct);

                    inventory_table_view.refresh();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product has been updated!");
                    alert.showAndWait();

                    clearBtn();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("No product selected!");
                    alert.showAndWait();
                }
            }
        }
    }

    private void updateProductInJsonFile(ProductObject updatedProduct) {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String productId = jsonObject.get("product_id").getAsString();

                if (productId.equals(updatedProduct.getProduct_id())) {
                    jsonObject.addProperty("product_name", updatedProduct.getProduct_name());
                    jsonObject.addProperty("type", updatedProduct.getType());
                    jsonObject.addProperty("stock", updatedProduct.getStock());
                    jsonObject.addProperty("price", updatedProduct.getPrice());
                    jsonObject.addProperty("status", updatedProduct.getStatus());
                    jsonObject.addProperty("product_image", updatedProduct.getProduct_image());
                    break;
                }
            }
            try (Writer writer = new FileWriter(FILE_PATH)) {
                Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
                gsonPretty.toJson(jsonArray, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inventorySelectData() {
        ProductObject productObject = inventory_table_view.getSelectionModel().getSelectedItem();
        int num = inventory_table_view.getSelectionModel().getSelectedIndex();

        if (productObject != null) {
            inventory_product_id.setText(productObject.getProduct_id());
            inventory_price.setText(String.valueOf(productObject.getPrice()));
            inventory_product_name.setText(productObject.getProduct_name());
            inventory_stock.setText(String.valueOf(productObject.getStock()));

            inventory_choose_status.setValue(productObject.getStatus());
            inventory_choose_type.setValue(productObject.getType());

            String path = productObject.getProduct_image();
            if (path != null && !path.isEmpty()) {
                String imagePath = path;
                Image image = new Image(imagePath, 200, 200, false, true);
                inventory_image_view.setImage(image);
            } else {
                inventory_image_view.setImage(null);
            }
        }
    }

    public void clearBtn() {
        inventory_product_id.setText("");
        inventory_price.setText("");
        inventory_product_name.setText("");
        inventory_choose_status.getSelectionModel().clearSelection();
        inventory_choose_type.getSelectionModel().clearSelection();
        inventory_stock.setText("");
        inventory_image_view.setImage(null);
    }

    public List<ProductObject> loadProductsFromJsonFile() {
        ProductObjectDAO productObjectDAO = new ProductObjectDAO();
        return productObjectDAO.loadProductData();
    }

    public void populateTableView() {
        List<ProductObject> products = loadProductsFromJsonFile();


        inventory_col_IDProduct.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        inventory_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_ProductName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        inventory_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        if (products != null) {
            ObservableList<ProductObject> observableProducts = FXCollections.observableArrayList(products);

            inventory_table_view.setItems(observableProducts);
        } else {
            inventory_table_view.getItems().clear();
        }
    }

    public void addBtn() {
        if (inventory_product_id.getText().isEmpty() ||
                inventory_product_name.getText().isEmpty() ||
                inventory_choose_type.getSelectionModel().getSelectedItem() == null ||
                inventory_stock.getText().isEmpty() || inventory_price.getText().isEmpty() ||
                inventory_choose_status.getSelectionModel().getSelectedItem() == null ||
                inventory_image_view.getImage() == null ||
                inventory_image_view.getImage().getUrl() == null ||
                inventory_image_view.getImage().getUrl().isEmpty()) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields and import picture");
            alert.showAndWait();
        } else {
            String productId = inventory_product_id.getText();
            String productName = inventory_product_name.getText();
            // Check if the product already exists
            if (isProductExists(productId, productName)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Product " + inventory_product_id.getText() + " already exists, or the product name is already taken");
                alert.showAndWait();
            } else {
                String type = inventory_choose_type.getSelectionModel().getSelectedItem().toString();
                Integer stock = Integer.parseInt(inventory_stock.getText());
                Double price = Double.parseDouble(inventory_price.getText());
                String status = inventory_choose_status.getSelectionModel().getSelectedItem().toString();
                String productImage = inventory_image_view.getImage().getUrl();

                ProductObject productObject = new ProductObject(productId, productName, type, stock, price, status, productImage);

                File file = new File(FILE_PATH);
                if (file.exists()) {
                    productObjectDAO.appendDataToFile(productObject);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product has been added!");
                    alert.showAndWait();

                    populateTableView();
                    clearBtn();
                }
            }
        }
    }

    public boolean isProductExists(String productId, String productName) {
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            ProductObject[] productObjects = gson.fromJson(fileReader, ProductObject[].class);
            if (productObjects != null) {
                for (ProductObject product : productObjects) {
                    if (product.getProduct_id().equals(productId) || product.getProduct_name().equals(productName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<ProductObject> menuGetData() {
        ObservableList<ProductObject> cardListData = FXCollections.observableArrayList();
        Gson gson = new GsonBuilder().create();
        try (Reader reader = new FileReader(FILE_PATH)) {
            ProductObject[] productArray = gson.fromJson(reader, ProductObject[].class);
            if (productArray != null) {
                cardListData.addAll(productArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cardListData;
    }

    public void menuDisplayCard() {
        menu_gridPane.getChildren().clear();

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int columnCount = 3;
        int rowCount = (int) Math.ceil(cardListData.size() / (double) columnCount);

        menu_gridPane.getChildren().clear();
        menu_gridPane.getColumnConstraints().clear();
        menu_gridPane.getRowConstraints().clear();

        for (int i = 0; i < columnCount; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / columnCount);
            menu_gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < rowCount; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            menu_gridPane.getRowConstraints().add(rowConstraints);
        }

        int cardIndex = 0;

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (cardIndex >= cardListData.size()) {
                    break;
                }

                try {
                    FXMLLoader load = new FXMLLoader();
                    load.setLocation(getClass().getResource("/com/hit/driver/cardProduct.fxml"));
                    AnchorPane pane = load.load();
                    CardProductController cardProductController = load.getController();
                    cardProductController.setMainFormController(this);
                    cardProductController.setData(cardListData.get(cardIndex));

                    menu_gridPane.add(pane, column, row);
                    GridPane.setMargin(pane, new Insets(10));

                    cardIndex++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void importBtn() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) inventory_importBtn.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            inventory_image_view.setImage(image);
        }
    }

    public void inventoryStatusList() {
        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statusL);
        inventory_choose_status.setItems(listData);
    }

    public void inventoryTypeList() {
        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_choose_type.setItems(listData);
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboardBtn) {
            form_dashboard.setVisible(true);
            form_inventory.setVisible(false);
            form_menu.setVisible(false);
            clearMenuTableView();
            removeBtnPayDataFile();
            loadIncomeChart();
            displayNumberCustomers();
            dashboard_display_totalIncome.setText(String.valueOf(CustomerObjectDAO.calculateTotalIncomeFromFile()));

        } else if (event.getSource() == inventoryBtn) {
            form_dashboard.setVisible(false);
            form_inventory.setVisible(true);
            form_menu.setVisible(false);

            inventoryTypeList();
            inventoryStatusList();
            populateTableView();
            clearMenuTableView();
            removeBtnPayDataFile();

        } else if (event.getSource() == menuBtn) {
            form_dashboard.setVisible(false);
            form_inventory.setVisible(false);
            form_menu.setVisible(true);

            readLastCustomerIdFromFile();
            menuDisplayCard();
        }
    }

    public void logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                signoutBtn.getScene().getWindow().hide();

                FXMLLoader fxmlLoader = new FXMLLoader(AppController.class.getResource("EntranceForm.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load(), 320, 240);
                stage.setTitle("Restaurant Management System");
                stage.setMinHeight(439);
                stage.setMinWidth(600);
                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PayObject> loadCustomersFromJsonFile() {
        try (Reader reader = new FileReader("payData.json")) {
            Gson gson = new Gson();
            Type customerListType = new TypeToken<List<PayObject>>() {
            }.getType();
            List<PayObject> payData = gson.fromJson(reader, customerListType);

            return payData;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void populateMenuTableView() {
        List<PayObject> payData = loadCustomersFromJsonFile();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));


        if (payData != null) {
            ObservableList<PayObject> observableCustomer = FXCollections.observableArrayList(payData);
            menu_tableView.setItems(observableCustomer);
            calculatePay();
        } else {
            menu_tableView.getItems().clear();
        }
    }

    public void calculatePay() {
        double total = 0.0;

        for (Object item : menu_tableView.getItems()) {
            PayObject payObject = (PayObject) item;
            double price = payObject.getPrice();
            int quantity = payObject.getQuantity();
            double itemTotal = price * quantity;
            total += itemTotal;
        }

        menu_total.setText(String.format("%.2f", total));
    }

    public void calculateChange() {
        String amountText = menu_amount.getText();
        double total = Double.parseDouble(menu_total.getText());
        if (!amountText.isEmpty()) {
            double amount = Double.parseDouble(amountText);
            double change = amount - total;
            menu_change.setText(String.format("%.2f", change));
        } else {
            menu_change.setText("");
        }
    }

    public void removeBtnPayDataFile() {
        if (form_menu.isVisible()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove the data?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                eraseDataFile();
            }
        } else {
            eraseDataFile();
        }
    }

    private void eraseDataFile() {
        try (FileWriter fileWriter = new FileWriter("paydata.json")) {
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error occurred while removing the receipt.");
            errorAlert.showAndWait();
        }

        menu_tableView.getItems().clear();
        menu_total.setText("0.0");
    }

    public void receipt() {

        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("Items:\n");

        for (Object item : menu_tableView.getItems()) {
            PayObject payObject = (PayObject) item;
            String itemName = payObject.getProductName();
            double itemPrice = payObject.getPrice();
            int itemQuantity = payObject.getQuantity();
            double itemTotal = itemPrice * itemQuantity;

            receiptContent.append("- ").append(itemName).append(": ").append(itemPrice).append(" x ").append(itemQuantity).append(" = ").append(itemTotal).append("\n");
        }

        double total = Double.parseDouble(menu_total.getText());
        String amountText = menu_amount.getText();
        double amount = Double.parseDouble(amountText);
        double change = amount - total;
        receiptContent.append("\nTotal: ").append(total).append("\n");
        receiptContent.append("Amount Paid: ").append(amount).append("\n");
        receiptContent.append("Change: ").append(change);

        Alert receiptAlert = new Alert(Alert.AlertType.INFORMATION);
        receiptAlert.setTitle("Receipt");
        receiptAlert.setHeaderText(null);
        receiptAlert.setContentText(receiptContent.toString());
        receiptAlert.showAndWait();

        menu_tableView.getItems().clear();
        menu_total.setText("0.0");
        menu_amount.setText("0.0");
        menu_change.setText("0.0");
    }

    public void payBtn() {
        double total = Double.parseDouble(menu_total.getText());
        double amountPaid = Double.parseDouble(menu_amount.getText());
        double change = amountPaid - total;

        CustomerObject customerObject = new CustomerObject(total, amountPaid, change);


        receipt();
        customerObjectDAO.appendCustomerToFile(customerObject);
        PayObjectDAO.clearPayDataFile();
        CustomerObject.setLastCustomerId(customerObject.getCustomerId());
    }

    public void clearMenuTableView() {
        menu_tableView.getItems().clear();
        menu_total.setText("0.0");
        menu_amount.setText("0.0");
        menu_change.setText("0.0");
    }

    public void displayNumberCustomers(){
        String number_customers = String.valueOf(CustomerObject.getLastCustomerId());
        dashboard_display_num_customers.setText(number_customers);
    }

    private ObservableList<XYChart.Series<String, Number>> loadCustomerData() {
        ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<CustomerObject>>() {
            }.getType();
            List<CustomerObject> customerList = gson.fromJson(new FileReader("customerData.json"), listType);

            for (CustomerObject customer : customerList) {
                String customerId = String.valueOf(customer.getCustomerId());
                Double incomeAmount = customer.getAmountPaid();

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.getData().add(new XYChart.Data<>(customerId, incomeAmount));

                data.add(series);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void loadIncomeChart() {
        ObservableList<XYChart.Series<String, Number>> data = loadCustomerData();
        barChart.setData(data);
        barChart.setLegendVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadIncomeChart();
        readLastCustomerIdFromFile();
        dashboard_display_num_customers.setText(String.valueOf(getLastCustomerId()));
        dashboard_display_totalIncome.setText(String.valueOf(getTotalIncome()));
    }
}


