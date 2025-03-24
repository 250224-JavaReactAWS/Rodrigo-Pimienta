package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.dtos.response.SuccessMessage;
import com.revature.models.CartItem;
import com.revature.services.CartItemService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartItemController {

    private final Logger logger = LoggerFactory.getLogger(CartItemController.class);

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService){
        this.cartItemService=cartItemService;
    }

    public void gatUserCartHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        ctx.json(cartItemService.getUserCartItems(ctx.sessionAttribute("userId")));
    }

    public void addCartItemHandler(Context ctx){

        CartItem requestCartItem = ctx.bodyAsClass(CartItem.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        /*
         TODO BEFORE INSERT CHECK IF THE PRODUCT EXISTS AND IT'S ACTIVE
         TODO BEFORE INSERT CHECK IF THE PRODUCT IT'S ALREADY ON THE CART
         */

        if(!cartItemService.isProductExist(requestCartItem.getProductId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product is not in the catalog, please select a new one"));
            logger.warn("Register attempt made for taken product ID: "+ requestCartItem.getProductId());
            return;
        }

        if(cartItemService.isProductActive(requestCartItem.getProductId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product is not available, please select a new one"));
            logger.warn("Register attempt made for taken product ID: "+ requestCartItem.getProductId());
            return;
        }

        if(!cartItemService.isProductRelatedWithUserCart(requestCartItem.getProductId(),ctx.sessionAttribute("userId"))){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product is already int the cart, please select a new one"));
            logger.warn("Register attempt made for existing product ID: "+ requestCartItem.getProductId());
            return;
        }

        // TODO CHECK IF THIS VALIDATION IT'S CORRECT
        if(requestCartItem.getQuantity() <= 0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Quantity is not valid, should be grader to 0, please select a new one"));
            logger.warn("Register attempt made for invalid quantity product ID: "+ requestCartItem.getProductId());
            return;
        }

        CartItem registerCartItem = cartItemService.registerCartItem(
                ctx.sessionAttribute("userId"),
                requestCartItem.getProductId(),
                requestCartItem.getQuantity()
        );

        if(registerCartItem == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New cart item registered for product: "+requestCartItem.getProductId());

        ctx.status(201);
        ctx.json(registerCartItem);

    }

    public void updateCartItemHandler(Context ctx){
        CartItem requestCartItem = ctx.bodyAsClass(CartItem.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String cartItemIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (cartItemIdFromPath == null || cartItemIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Cart Item ID is required in the path."));
            return;
        }

        int cartItemId;
        try {
            cartItemId = Integer.parseInt(cartItemIdFromPath); // Parse userId as an integer
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Cart Item ID format. Must be a number."));
            return;
        }

        // TODO BEFORE INSERT CHECK IF THE PRODUCT IT'S ALREADY ON THE CART
        if(cartItemService.isProductRelatedWithUserCart(requestCartItem.getProductId(),ctx.sessionAttribute("userId"))){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product is not int the cart, please select a new one"));
            logger.warn("Register attempt made for existing product ID: "+ requestCartItem.getProductId());
            return;
        }


        // TODO CHECK IF THE CART ITEM IT'S RELATED TO THE USER AND THE PRODUCT
        if(!cartItemService.isCartItemRelatedWithUserAndProduct(cartItemId, ctx.sessionAttribute("userId"), requestCartItem.getProductId())){
            ctx.status(403);
            ctx.json(new ErrorMessage("You are not allow to update this item , please select a new one"));
            logger.warn("Register attempt made for update cart ID: "+ cartItemId);
            return;
        }

        /*
         TODO BEFORE UPDATE CHECK IF THE PRODUCT EXISTS AND IS ACTIVE
         */
        if(!cartItemService.isProductExist(requestCartItem.getProductId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product is not in catalog, please select a new one"));
            logger.warn("Register attempt made for taken product ID: "+ requestCartItem.getProductId());
            return;
        }

        if(cartItemService.isProductActive(requestCartItem.getProductId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product is not available, please select a new one"));
            logger.warn("Register attempt made for taken product ID: "+ requestCartItem.getProductId());
            return;
        }

        // TODO CHECK IF THIS VALIDATION IT'S CORRECT
        if(requestCartItem.getQuantity() < 0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Quantity is not valid, should be grader or equal to 0, please select a new one"));
            logger.warn("Register attempt made for invalid quantity product ID: "+ requestCartItem.getProductId());
            return;
        }

        requestCartItem.setCartItemId(cartItemId);

        CartItem registedCartItem = cartItemService.updateCartItem(
                requestCartItem.getCartItemId(),
                ctx.sessionAttribute("userId"),
                requestCartItem.getProductId(),
                requestCartItem.getQuantity()
        );

        if(registedCartItem == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New cart item registered for product: "+requestCartItem.getProductId());

        ctx.status(200);
        ctx.json(registedCartItem);
    }

    public void  deleteUserCartItemHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }


        boolean registedCartItem = cartItemService.deleteUserCartItems(ctx.sessionAttribute("userId"));

        if(!registedCartItem){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Deleted user cart items  registered for userId: "+ctx.sessionAttribute("userId"));

        ctx.status(200);
        ctx.json(new SuccessMessage("Deleted cart item successfully"));
    }

    public void deleteCartItemHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String cartItemIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (cartItemIdFromPath == null || cartItemIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Cart Item ID is required in the path."));
            return;
        }

        int cartItemId;
        try {
            cartItemId = Integer.parseInt(cartItemIdFromPath); // Parse userId as an integer
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Cart Item ID format. Must be a number."));
            return;
        }

        if(!cartItemService.isCartItemRelatedWithUser(cartItemId, ctx.sessionAttribute("userId"))){
            ctx.status(403);
            ctx.json(new ErrorMessage("You are not allow to update this item , please select a new one"));
            logger.warn("Register attempt made for update cart ID: "+ cartItemId);
            return;
        }

        boolean registedCartItem = cartItemService.deleteCartItem(cartItemId);

        if(!registedCartItem){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Deleted cart item registered for userId: "+ctx.sessionAttribute("userId"));

        ctx.status(200);
        ctx.json(new SuccessMessage("Deleted cart items successfully"));
    }
}
