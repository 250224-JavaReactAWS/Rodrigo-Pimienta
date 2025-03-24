package com.revature.models;

public class CartItem {
    private int cartItemId;
    private int userId;
    private int productId;
    private int quantity;
    private String product;
    private Double productPrice;
    private boolean productStatus;
    private int productStock;
    private Double total;
    private String category;


    public CartItem() {
    }

    public CartItem(int cartItemId, int userId, int productId, int quantity) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItem(int cartItemId, int userId, int productId, int quantity, String category, String product, Double productPrice, int productStock, boolean productStatus, Double total) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.product =product;
        this.productPrice = productPrice;
        this.productStock=productStock;
        this.productStatus = productStatus;
        this.total=total;
        this.category = category;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double product_price) {
        this.productPrice = product_price;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public boolean isProductStatus() {
        return productStatus;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", product='" + product + '\'' +
                ", productPrice=" + productPrice +
                ", productStatus=" + productStatus +
                ", productStock=" + productStock +
                ", total=" + total +
                ", category='" + category + '\'' +
                '}';
    }
}
