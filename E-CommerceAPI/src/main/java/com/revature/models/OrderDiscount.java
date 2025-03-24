package com.revature.models;

public class OrderDiscount {
    private int discountId;
    private int orderId;
    private double percentage;
    private double amount;
    private int order;
    private String code;


    public OrderDiscount() {
    }

    public OrderDiscount(int discountId, double percentage, int order) {
        this.discountId = discountId;
        this.percentage = percentage;
        this.order = order;
    }

    public OrderDiscount( int discountId, int orderId, double percentage, double amount, int order) {
        this.discountId = discountId;
        this.amount = amount;
        this.percentage=percentage;
        this.orderId = orderId;
        this.order = order;
    }

    public OrderDiscount( int discountId, int orderId, double percentage, double amount, int order, String code) {
        this.discountId = discountId;
        this.orderId = orderId;
        this.percentage = percentage;
        this.amount = amount;
        this.order = order;
        this.code = code;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setDiscountAmount(float amount) {
        this.amount = amount;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
