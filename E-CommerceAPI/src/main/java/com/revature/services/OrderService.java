package com.revature.services;

import com.revature.dtos.response.OrderCalculations;
import com.revature.models.*;
import com.revature.repos.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final CartItemDAO cartItemDAO;
    private final OrderDiscountDAO orderDiscountDAO;
    private final ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, OrderItemDAO orderItemDAO, CartItemDAO cartItemDAO, OrderDiscountDAO orderDiscountDAO, ProductDAO productDAO){
        this.orderDAO=orderDAO;
        this.orderItemDAO=orderItemDAO;
        this.cartItemDAO=cartItemDAO;
        this.orderDiscountDAO=orderDiscountDAO;
        this.productDAO=productDAO;
    }

    // VALIDATION AVAILABILITY
    public List<CartItem> isCartEmpty(int userId){
        List<CartItem> ci = cartItemDAO.getCartItemsByUser(userId);

        if(ci == null){
            return null;
        }

        /*
            Validations
            Status product active
            Quantity grader than 0
         */

        List<CartItem> filteredCartItems = ci.stream()
                .filter(cartItem -> cartItem.isProductStatus() && cartItem.getQuantity() > 0 && cartItem.getProductStock() > cartItem.getQuantity())
                .toList();

        return filteredCartItems;
    }

    // CALCULATIONS
    public OrderCalculations calculateTotalDiscountAndApply(List<OrderDiscount> discounts, List<CartItem> cartItems) {
        /*
            Steps to create an order
            1.- Calculated the subTotal
            2.- Pass throw all the discount to set the amount of discount for each one of then, and calculated the total discount
            3.- Calculated the total (subTotal - totalDiscount)
         */

        Double subTotal = cartItems.stream().reduce(0.0,(sum, cartItem) -> sum + cartItem.getTotal(), Double::sum);
        final Double[] total = {subTotal};
        final Double[] totalDiscount = {0.0};

        discounts.forEach(discount ->{
            Double amount = (total[0] * discount.getPercentage()) / 100;
            discount.setAmount(amount);
            totalDiscount[0] += amount;
            total[0] -= amount;
        });

        return new OrderCalculations(total[0], subTotal,totalDiscount[0], discounts);
    }


    public Order registerOrder(int userId, int addressId, Double total, Double subTotal, Double discountTotal,  List<CartItem> cartItems, List<OrderDiscount>  discounts){


        Order orderToSave = new Order(0,userId, addressId,total,discountTotal,subTotal);
        Order newOrder = orderDAO.create(orderToSave);
        if(newOrder == null){
            return null;
        }

        int orderId = newOrder.getOrderId();


        cartItems.forEach(cartItem -> {

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductPrice());
            orderItem.setOrderId(orderId);

            OrderItem oi =  orderItemDAO.create(orderItem);

            if(oi == null){
                return;
            }

            int stock =  cartItem.getProductStock() - cartItem.getQuantity();
            Product p = productDAO.updateStockById(cartItem.getProductId(), stock);

            boolean delete = cartItemDAO.deleteCartItemByProductAndUser(userId,cartItem.getProductId());
        });


        // Now I have to create the orderItem and the ordersDiscounts
        discounts.forEach( discount -> {
            discount.setOrderId(orderId);
            OrderDiscount od =  orderDiscountDAO.create(discount);
        });

        newOrder.setItems(orderItemDAO.getOrderItemsBy(orderId));
        newOrder.setDiscounts(orderDiscountDAO.getOrderDiscounts(orderId));
        return newOrder;
    }


    public List<Order> getAllOrders(){

        List<Order> orders = orderDAO.getAll();

        return orders.stream()
                .map(order -> {
                    order.setItems(orderItemDAO.getOrderItemsBy(order.getOrderId()));
                    order.setDiscounts(orderDiscountDAO.getOrderDiscounts(order.getOrderId()));
                    return order;
                })
                .collect(Collectors.toList());
    }

    public List<Order> getUserOrders(int userId){
        List<Order> orders = orderDAO.getOrdersByUser(userId);

        return orders.stream()
                .map(order -> {
                    order.setItems(orderItemDAO.getOrderItemsBy(order.getOrderId()));
                    order.setDiscounts(orderDiscountDAO.getOrderDiscounts(order.getOrderId()));
                    return order;
                })
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByStatus(OrderStatus status){
        List<Order> orders = orderDAO.getOrdersByStatus(status);

        return orders.stream()
                .map(order -> {
                    order.setItems(orderItemDAO.getOrderItemsBy(order.getOrderId()));
                    order.setDiscounts(orderDiscountDAO.getOrderDiscounts(order.getOrderId()));
                    return order;
                })
                .collect(Collectors.toList());
    }

    public Order getOrder(int orderId){
        Order order = orderDAO.getById(orderId);

        if(order == null){
            return null;
        }
        order.setItems(orderItemDAO.getOrderItemsBy(orderId));
        order.setDiscounts(orderDiscountDAO.getOrderDiscounts(orderId));
        return order;
    }

    public Order updateStatus(int orderId, OrderStatus status){
        return orderDAO.updateOrderStatus(orderId, status);
    }


}
