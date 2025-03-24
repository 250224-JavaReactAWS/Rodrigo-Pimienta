package com.revature.dtos.response;

import com.revature.models.OrderDiscount;

import java.util.List;

public class OrderCalculations {

    private double total;
    private double subTotal;
    private double discount;
    private List<OrderDiscount> discounts;

    public OrderCalculations(double total, double subTotal, double discount, List<OrderDiscount> discounts) {
        this.total = total;
        this.subTotal = subTotal;
        this.discount = discount;
        this.discounts = discounts;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<OrderDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<OrderDiscount> discounts) {
        this.discounts = discounts;
    }
}
