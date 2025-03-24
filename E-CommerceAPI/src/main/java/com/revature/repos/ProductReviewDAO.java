package com.revature.repos;

import com.revature.models.ProductReview;

import java.util.List;

public interface ProductReviewDAO extends GeneralDAO<ProductReview> {
    List<ProductReview> getProductReviewsByProduct(int productId);
    List<ProductReview> getUserReviews(int userId);

}
