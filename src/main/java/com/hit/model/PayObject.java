package com.hit.model;

public class PayObject {
    private static int lastAssignedID = 0;
    private int customerID;
    private Double price;
    private Integer quantity;
    private String product_name;


    public PayObject() {
        this.customerID = generateNextID();
    }

    private static int generateNextID() {
        lastAssignedID++;
        return lastAssignedID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String productName) {
        this.product_name = productName;
    }
}
