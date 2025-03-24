package com.revature.services;

import com.revature.models.Category;
import com.revature.models.Product;
import com.revature.repos.CategoryDAO;
import com.revature.repos.ProductDAO;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;

    private final CategoryDAO categoryDAO;

    public ProductService(ProductDAO productDAO, CategoryDAO categoryDAO){
        this.productDAO=productDAO;
        this.categoryDAO=categoryDAO;
    }

    // validation availability
    public boolean isCategoryExist(int categoryId){
        return categoryDAO.getById(categoryId) == null;
    }

    public boolean isCategoryActive(int categoryId){
        Category c = categoryDAO.getById(categoryId);
        return !c.isActive();
    }

    public Product registerNewProduct(int categoryId, String name, String description, Double price, int stock){
        Product newProduct = new Product(0, categoryId, name, description,price,stock);
        return productDAO.create(newProduct);
    }

    public List<Product> getAllProducts(){
        return productDAO.getAll();
    }

    public Product getProduct(int productId){
        return productDAO.getById(productId);
    }

    public Product updateProduct(int productId, int categoryId, String name, String description, Double price, int stock){
        Product productToUpdate = new Product(productId, categoryId, name, description,price,stock);
        return productDAO.update(productToUpdate);
    }


    public Product updateStatus(int productId, boolean status) {

        return productDAO.updateStatusById(productId, status);
    }

}
