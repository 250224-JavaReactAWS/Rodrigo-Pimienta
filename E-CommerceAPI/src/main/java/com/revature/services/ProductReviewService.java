package com.revature.services;

import com.revature.models.ProductReview;
import com.revature.repos.OrderItemDAO;
import com.revature.repos.ProductReviewDAO;

import java.util.List;

public class ProductReviewService {

    private final ProductReviewDAO productReviewDAO;

    private final OrderItemDAO orderItemDAO;

    public  ProductReviewService(ProductReviewDAO productReviewDAO, OrderItemDAO orderItemDAO){
        this.productReviewDAO=productReviewDAO;
        this.orderItemDAO=orderItemDAO;
    }

    public boolean hasUserBoughtThisProduct(int userId, int productId){
        return orderItemDAO.getItemByProductAndUser(userId,productId);
    }

    public ProductReview registerReview(int userId, int productId, String comment, short rating){
        ProductReview newProductReview = new ProductReview(0, userId, productId, comment, rating);
        return productReviewDAO.create(newProductReview);
    }

    public List<ProductReview> getAllProductsReviews(){
        return productReviewDAO.getAll();
    }

    public List<ProductReview> getUserReviews(int userId){
        return productReviewDAO.getUserReviews(userId);
    }

    public List<ProductReview> getAllProductReviews(int productId){
        return productReviewDAO.getProductReviewsByProduct(productId);
    }

    public ProductReview getReview(int reviewId){
        return productReviewDAO.getById(reviewId);
    }



}
