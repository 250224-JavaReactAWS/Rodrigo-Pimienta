package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.ProductReview;
import com.revature.services.ProductReviewService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProductReviewsController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductReviewService productReviewService;

    public ProductReviewsController(ProductReviewService productReviewService){
        this.productReviewService=productReviewService;
    }

    public void getAllReviewsHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        ctx.json(productReviewService.getAllProductsReviews());
    }

    public void getUserReviewsHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        int userId = ctx.sessionAttribute("userId");
        ctx.json(productReviewService.getUserReviews(userId));
    }


    public void getProductReviewsHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
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

        ctx.json(productReviewService.getAllProductReviews(productId));
    }

    public void registerNewReviewHandler(Context ctx){
        ProductReview requestReview = ctx.bodyAsClass(ProductReview.class);

        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        if(!productReviewService.hasUserBoughtThisProduct(ctx.sessionAttribute("userId"), requestReview.getProductId())){
            ctx.status(400);
            ctx.json(new ErrorMessage("The user has not bought this item before, please select a new one"));
            logger.warn("Register attempt made for not bought product id: "+ requestReview.getProductId());
            return;
        }

        ProductReview registerReview = productReviewService.registerReview(
                ctx.sessionAttribute("userId"),
                requestReview.getProductId(),
                requestReview.getComment(),
                requestReview.getRating()
        );

        if(registerReview == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New product review registered for product: "+registerReview.getProductId());

        ctx.status(201);
        ctx.json(registerReview);
    }

    public void getReviewHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }

        String reviewIdFormPath = ctx.pathParam("id");

        if(reviewIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Review ID is required in the path."));
            return;
        }

        int reviewId;
        try{
            reviewId = Integer.parseInt(reviewIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid review ID format. Must be a number."));
            return;
        }

        System.out.println(reviewId);

        ProductReview pr =  productReviewService.getReview(reviewId);

        if(pr == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Not existing review ID."));
            return;
        }

        ctx.json(pr);
    }

}


