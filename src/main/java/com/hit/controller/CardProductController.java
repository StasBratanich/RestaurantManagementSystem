package com.hit.controller;

import com.hit.dao.PayObjectDAO;
import com.hit.dao.ProductObjectDAO;
import com.hit.model.PayObject;
import com.hit.model.ProductObject;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class CardProductController implements Initializable {

    @FXML
    private ImageView product_imageView;

    @FXML
    private Label product_name;

    @FXML
    private Label product_price;

    @FXML
    private Spinner<Integer> product_spinner;

    private ProductObject productData;

    private Image image;

    private String prodID;

    private SpinnerValueFactory<Integer> spin;

    private static PayObject currentCustomer;

    private MainFormController mainFormController;

    public void setMainFormController(MainFormController mainFormController) {
        this.mainFormController = mainFormController;
    }

    private PayObjectDAO payObjectDAO = new PayObjectDAO();

    private ProductObjectDAO productObjectDAO = new ProductObjectDAO();


    public void setData(ProductObject productData){
        this.productData = productData;
        prodID = productData.getProduct_id();
        product_name.setText(productData.getProduct_name());
        product_price.setText(String.valueOf(productData.getPrice()));
        String path = productData.getProduct_image();
        image = new Image(path, 200, 200, false, true);
        product_imageView.setImage(image);
    }

    public void setQuantity(){
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        product_spinner.setValueFactory(spin);
    }

    public void addBtn() {
        int spinnerValue = product_spinner.getValue();
        String productName = product_name.getText();
        double productPrice = Double.parseDouble(product_price.getText());

        if (currentCustomer != null) {
            currentCustomer.setProductName(productName);
            currentCustomer.setPrice(productPrice);
            currentCustomer.setQuantity(spinnerValue);
        } else {
            currentCustomer = new PayObject();
            currentCustomer.setProductName(productName);
            currentCustomer.setPrice(productPrice);
            currentCustomer.setQuantity(spinnerValue);
        }

        payObjectDAO.appendCustomerToFile(currentCustomer);

        if (mainFormController != null) {
            mainFormController.populateMenuTableView();
        }

        subtractQuantityFromStock(productName, spinnerValue);
    }

    public void subtractQuantityFromStock(String productName, int quantity) {
        List<ProductObject> products = productObjectDAO.loadProductData();

        for (ProductObject product : products) {
            if (product.getProduct_name().equals(productName)) {
                int newStock = product.getStock() - quantity;
                product.setStock(newStock);
                break;
            }
        }
        productObjectDAO.saveProductData(products);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setQuantity();
    }
}
