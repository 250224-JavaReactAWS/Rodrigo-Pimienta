package com.revature.repos;

import com.revature.models.OrderItem;

import java.util.List;

public interface OrderItemDAO extends GeneralDAO<OrderItem> {
    List<OrderItem> getOrderItemsBy(int orderId);
    boolean getItemByProductAndUser(int userId, int ProductId);
}
