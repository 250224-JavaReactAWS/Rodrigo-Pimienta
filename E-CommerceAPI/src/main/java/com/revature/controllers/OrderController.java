package com.revature.controllers;

import com.revature.dtos.request.OrderRegisterRequest;
import com.revature.dtos.request.StatusOrderUpdateRequest;
import com.revature.dtos.response.ErrorMessage;
import com.revature.dtos.response.OrderCalculations;
import com.revature.models.*;
import com.revature.services.DiscountService;
import com.revature.services.OrderService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    private final DiscountService discountService;

    public OrderController(OrderService orderService, DiscountService discountService){
        this.orderService=orderService;
        this.discountService=discountService;
    }


    public void getAllOrdersHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            ctx.status(403);
            ctx.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }
        ctx.json(orderService.getAllOrders());
    }

    public void getUserOrdersHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        ctx.json(orderService.getUserOrders(ctx.sessionAttribute("userId")));
    }

    public void getOrdersByStatusHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            ctx.status(403);
            ctx.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }

        String statusOrder = ctx.pathParam("status");

        if (statusOrder.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Status is required in the path."));
            return;
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(statusOrder.toUpperCase());

            ctx.json(orderService.getOrdersByStatus(orderStatus));
        } catch (IllegalArgumentException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid order status. Must be one of: PENDING, SHIPPED, DELIVERED."));
        }
    }

    public void getOrderHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        String orderIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (orderIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Order ID is required in the path."));
            return;
        }

        int orderId;
        try{
            orderId = Integer.parseInt(orderIdFromPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Order ID format. Must be a number."));
            return;
        }

        Order order = orderService.getOrder(orderId);

        if(order ==null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Order ID. The order does not exist."));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            int userId = ctx.sessionAttribute("userId");
            if(order.getUserId() != userId) {
                ctx.status(403);
                ctx.json(new ErrorMessage("You dont have permission to consult this order."));
                return;
            }
        }


        ctx.json(order);
    }

    public void registerNewOrderHandler(Context ctx){
        OrderRegisterRequest requestOrder = ctx.bodyAsClass(OrderRegisterRequest.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }
        int userId= ctx.sessionAttribute("userId");

        // check if the cartItem have atLeast one validItem

        List<CartItem> cartItems = orderService.isCartEmpty(userId);

        if(cartItems == null || cartItems.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("The user cart does not have a valid product. Please add a valid one"));
            return;
        }

        // init and check discounts
        List<OrderDiscount> discounts = new ArrayList<>();
        int order =1;
        for(String discount: requestOrder.discounts()){
            if(!discountService.isDiscountAvailable(discount,userId)){
                ctx.status(400);
                ctx.json(new ErrorMessage("THe discount code "+ discount+" is invalid. Please select a new one"));
                return;
            }

            Discount d = discountService.getDiscountByCode(discount);
            OrderDiscount od = new OrderDiscount(d.getDiscountId(), d.getPercentage(), order);
            order++;
            discounts.add(od);
        }

        OrderCalculations orderCalculations = orderService.calculateTotalDiscountAndApply(discounts,cartItems);

        Order registerOrder = orderService.registerOrder(
                userId,
                requestOrder.addressId(),
                orderCalculations.getTotal(),
                orderCalculations.getSubTotal(),
                orderCalculations.getDiscount(),
                cartItems,
                orderCalculations.getDiscounts()
        );

        if(registerOrder == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

//        List<OrderItem> itemsOrder = orderService.getOrderItems(registerOrder.getOrderId());
//        List<OrderDiscount> discountsOrder = orderService.getOrderDiscounts(registerOrder.getOrderId());

        logger.info("New order registered with name: "+registerOrder.getOrderId());

        ctx.status(201);
        ctx.json(registerOrder);
    }

    public void updateStatusHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        if (ctx.sessionAttribute("role") != UserRole.ADMIN){
            ctx.status(403);
            ctx.json(new ErrorMessage("You must be an admin to access this endpoint!"));
            return;
        }

        String orderIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (orderIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Order ID is required in the path."));
            return;
        }

        int orderId;
        try{
            orderId = Integer.parseInt(orderIdFromPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Order ID format. Must be a number."));
            return;
        }

        StatusOrderUpdateRequest request = ctx.bodyAsClass(StatusOrderUpdateRequest.class);

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(request.status().toUpperCase());

            Order registeredOrder = orderService.updateStatus(
                    orderId,
                    orderStatus
            );

            if(registeredOrder == null){
                ctx.status(500);
                ctx.json(new ErrorMessage("Something went wrong!"));
                return;
            }

            logger.info("Order status updated with order ID: "+orderId);

            ctx.status(200);
            ctx.json(registeredOrder);
        } catch (IllegalArgumentException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid order status. Must be one of: PENDING, SHIPPED, DELIVERED."));
        }

    }

}
