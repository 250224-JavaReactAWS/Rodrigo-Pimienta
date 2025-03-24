package com.revature.services;

import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.repos.CartItemDAO;
import com.revature.repos.ProductDAO;

import java.util.List;

public class CartItemService {


    private final CartItemDAO cartItemDAO;

    private final ProductDAO productDAO;

    public CartItemService(CartItemDAO cartItemDAO, ProductDAO productDAO){
        this.cartItemDAO=cartItemDAO;
        this.productDAO=productDAO;
    }

    public CartItem registerCartItem(int userId, int productId, int quantity){
      CartItem newCartItem = new CartItem(0, userId, productId, quantity);
      return cartItemDAO.create(newCartItem);
    }

    // Validation availability
    public boolean isProductExist(int productId){
        return productDAO.getById(productId) != null;
    }

    public boolean isProductActive(int productId){
        Product p = productDAO.getById(productId);
        return !p.isActive();
    }

    public boolean isCartItemRelatedWithUserAndProduct(int cartItem, int userId, int productId){
        CartItem ci = cartItemDAO.getCartItemByProductAndUser(userId, productId);
        return ci.getCartItemId() == cartItem;
    }

    public boolean isCartItemRelatedWithUser(int cartItem, int userId){
        CartItem ci = cartItemDAO.getById(cartItem);
        return ci.getUserId() == userId;
    }

    public boolean isProductRelatedWithUserCart(int productId, int userId){
        return cartItemDAO.getCartItemByProductAndUser(userId,productId) == null;
    }

    public List<CartItem> getUserCartItems(int userId){
        return cartItemDAO.getCartItemsByUser(userId);
    }

    public CartItem updateCartItem(int cartItemId, int userId, int productId,int quantity){
        CartItem cartItemToUpdate = new CartItem(cartItemId, userId, productId, quantity);
        return cartItemDAO.update(cartItemToUpdate);
    }

    public boolean deleteCartItem(int cartItemId){
        return cartItemDAO.deleteById(cartItemId);
    }

    public boolean deleteUserCartItems(int userId){
        return cartItemDAO.deleteCartItemByUser(userId);
    }

}
