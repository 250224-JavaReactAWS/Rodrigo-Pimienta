package com.revature.models;

import java.time.OffsetDateTime;

public class ProductReview {

    private int reviewId;
    private int userId;
    private int productId;
    private String comment;
    private short rating;
    private OffsetDateTime reviewDate;
    private String nameUser;
    private String product;
    private String category;

    public ProductReview() {
    }

    public ProductReview(int reviewId, int userId, int productId, String comment, short rating) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.comment = comment;
        this.rating = rating;
    }

    public ProductReview(int reviewId, int userId,  int productId, String comment, short rating, OffsetDateTime reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.comment = comment;
        this.productId = productId;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    public ProductReview(int reviewId, int userId, int productId, String comment, short rating, OffsetDateTime reviewDate, String nameUser, String product, String category) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.nameUser = nameUser;
        this.product = product;
        this.category = category;
    }

    public ProductReview(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public OffsetDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(OffsetDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
