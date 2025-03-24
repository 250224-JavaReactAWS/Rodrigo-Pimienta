package com.revature.repos;

import com.revature.models.OrderDiscount;

import java.util.List;

public interface OrderDiscountDAO extends GeneralDAO<OrderDiscount> {
    List<OrderDiscount> getOrderDiscounts(int orderId);
}
