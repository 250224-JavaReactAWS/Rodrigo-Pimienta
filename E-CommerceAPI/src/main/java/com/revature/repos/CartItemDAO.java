package com.revature.repos;

import com.revature.models.CartItem;

import java.util.List;

public interface CartItemDAO extends GeneralDAO<CartItem> {

    List<CartItem> getCartItemsByUser(int userId);
    CartItem getCartItemByProductAndUser(int userId, int productId);
    boolean deleteCartItemByUser(int userId);
    boolean deleteCartItemByProductAndUser(int userId, int productId);
}
