package com.revature.models;

public class Product {

    private int productId;
    private int categoryId;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private boolean active;
    private Double rating;


    public Product() {
    }

    public Product(int productId) {
        this.productId = productId;
    }

    public Product(int productId, int categoryId, String name, String description, Double price, int stock) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Product(int productId, int categoryId,String name, String description, Double price, int stock, boolean active, Double rating) {
        this.productId = productId;
        this.active = active;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.name = name;
        this.categoryId = categoryId;
        this.rating=rating;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
