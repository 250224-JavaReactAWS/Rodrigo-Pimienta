package com.revature.controllers;

import com.revature.dtos.request.StatusUpdateRequest;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Product;
import com.revature.models.UserRole;
import com.revature.services.ProductService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;


    public ProductController(ProductService productService){
        this.productService=productService;
    }

    public void getProductsHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        ctx.json(productService.getAllProducts());
    }

    public void getProductHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String productIdFormPath = ctx.pathParam("id");

        if(productIdFormPath == null || productIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product ID is required in the path."));
            return;
        }

        int productId;
        try{
            productId = Integer.parseInt(productIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Product ID format. Must be a number."));
            return;
        }

        Product product = productService.getProduct(productId);

        if(product == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        ctx.status(200);
        ctx.json(product);
    }

    public void registerNewProductHandler(Context ctx){
        Product requestProduct = ctx.bodyAsClass(Product.class);

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

        if(productService.isCategoryExist(requestProduct.getCategoryId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category id is not in the catalog, please select a new one"));
            logger.warn("Register attempt made for not existing category id: "+ requestProduct.getCategoryId());
            return;
        }

        if(productService.isCategoryActive(requestProduct.getCategoryId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category id is not available, please select a new one"));
            logger.warn("Register attempt made for not active category id: "+ requestProduct.getCategoryId());
            return;
        }

        // TODO ASK IF THIS VALIDATION SHOULD BE DONE IN THE SERVICES LAYER

        if(requestProduct.getPrice() < 0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Price is not valid, should be grader or equal to 0, please select a new one"));
            logger.warn("Register attempt made for invalid price: "+ requestProduct.getPrice());
            return;
        }


        if(requestProduct.getStock() < 0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Stock is not valid, should be grader or equal to 0, please select a new one"));
            logger.warn("Register attempt made for invalid stock: "+ requestProduct.getStock());
            return;
        }

        Product registedProduct = productService.registerNewProduct(
                requestProduct.getCategoryId(),
                requestProduct.getName(),
                requestProduct.getDescription(),
                requestProduct.getPrice(),
                requestProduct.getStock()
        );

        if(registedProduct == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New category  product registered with name: "+requestProduct.getName());

        ctx.status(201);
        ctx.json(registedProduct);
    }

    public void updateProductHandler(Context ctx){
        Product requestProduct = ctx.bodyAsClass(Product.class);

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

        String productIdFormPath = ctx.pathParam("id");

        if(productIdFormPath == null || productIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product ID is required in the path."));
            return;
        }

        int productId;
        try{
            productId = Integer.parseInt(productIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Product ID format. Must be a number."));
            return;
        }

        if(productService.isCategoryExist(requestProduct.getCategoryId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category id is not in the catalog, please select a new one"));
            logger.warn("Register attempt made for not existing category id: "+ requestProduct.getCategoryId());
            return;
        }

        if(productService.isCategoryActive(requestProduct.getCategoryId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category id is not available, please select a new one"));
            logger.warn("Register attempt made for not active category id: "+ requestProduct.getCategoryId());
            return;
        }

        // TODO ASK IF THIS VALIDATION SHOULD BE DONE IN THE SERVICES LAYER

        if(requestProduct.getPrice() < 0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Price is not valid, should be grader or equal to 0, please select a new one"));
            logger.warn("Register attempt made for invalid price: "+ requestProduct.getPrice());
            return;
        }


        if(requestProduct.getStock() < 0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Stock is not valid, should be grader or equal to 0, please select a new one"));
            logger.warn("Register attempt made for invalid stock: "+ requestProduct.getStock());
            return;
        }

        Product registedProduct = productService.updateProduct(
                productId,
                requestProduct.getCategoryId(),
                requestProduct.getName(),
                requestProduct.getDescription(),
                requestProduct.getPrice(),
                requestProduct.getStock()
        );

        if(registedProduct == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Product updated with categoryId: "+productId);

        ctx.status(200);
        ctx.json(registedProduct);
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

        String productIdFormPath = ctx.pathParam("id");

        if(productIdFormPath == null || productIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Product ID is required in the path."));
            return;
        }

        int productId;
        try{
            productId = Integer.parseInt(productIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Product ID format. Must be a number."));
            return;
        }

        StatusUpdateRequest request = ctx.bodyAsClass(StatusUpdateRequest.class);
        Product registeredProduct = productService.updateStatus(
                productId,
                request.status()
        );

        if(registeredProduct == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Product updated with category ID: "+productId);

        ctx.status(200);
        ctx.json(registeredProduct);
    }
}
