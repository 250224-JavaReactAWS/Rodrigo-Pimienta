package com.revature.repos;

import com.revature.models.Order;
import com.revature.models.OrderStatus;

import java.util.List;

public interface OrderDAO extends  GeneralDAO<Order> {
    List<Order> getOrdersByStatus(OrderStatus status);
    List<Order> getOrdersByUser(int userId);
    Order updateOrderStatus(int orderId, OrderStatus status);
//    List<OrderItem> getOrderItems(int orderId);
//    List<OrderDiscount> getOrderDiscounts(int orderId);
}
