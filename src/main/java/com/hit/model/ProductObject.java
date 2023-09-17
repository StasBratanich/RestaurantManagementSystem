package com.hit.model;

public class ProductObject {

    private String product_id;
    private String product_name;
    private String type;
    private Integer stock;
    private double price;
    private String status;
    private String product_image;
    private int quantity;

    public ProductObject(String product_id, String product_name, String type, Integer stock, Double price, String status, String product_image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.product_image = product_image;
    }

    public String getProduct_id() {return product_id;}

    public String getProduct_name() {return product_name;}

    public void setProduct_name(String product_name) {this.product_name = product_name;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public Integer getStock() {return stock;}

    public void setStock(Integer stock) {this.stock = stock;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public String getProduct_image() {return product_image;}

    public void setProduct_image(String product_image) {this.product_image = product_image;}
}

