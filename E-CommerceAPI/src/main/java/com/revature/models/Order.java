package com.revature.models;


import java.util.List;

public class Order {

    private int orderId;
    private int userId;
    private int addressId;
    private double subtotal;
    private double discount;
    private double totalPrice;
    private OrderStatus status;
    private String address;
    private List<OrderItem> items;
    private List<OrderDiscount> discounts;

    public Order( ) {
    }

    public Order(int orderId, int userId, int addressId, double total_price, double discount, double subtotal) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.totalPrice = total_price;
        this.discount = discount;
        this.subtotal = subtotal;
    }

    public Order(int orderId, int userId, int addressId, double subtotal, double discount, double totalPrice, OrderStatus status, String address) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.subtotal = subtotal;
        this.discount = discount;
        this.totalPrice = totalPrice;
        this.status = status;
        this.address = address;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<OrderDiscount> discounts) {
        this.discounts = discounts;
    }
}
