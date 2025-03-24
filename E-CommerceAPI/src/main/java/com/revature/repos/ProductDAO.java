package com.revature.repos;

import com.revature.models.Product;

import java.util.List;

public interface ProductDAO extends GeneralDAO<Product> {

    List<Product> getAllActive();
    List<Product> getProductsByCategory(int categoryId);
    Product updateStockById(int productId, int status);
    Product updateStatusById(int productId, boolean status);
}
