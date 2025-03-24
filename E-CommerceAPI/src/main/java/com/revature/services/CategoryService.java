package com.revature.services;

import com.revature.models.Category;
import com.revature.models.Product;
import com.revature.repos.CategoryDAO;
import com.revature.repos.ProductDAO;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final ProductDAO productDAO;

    public CategoryService(CategoryDAO categoryDAO,ProductDAO productDAO){
        this.categoryDAO=categoryDAO;
        this.productDAO=productDAO;
    }

    // Availability
    public boolean isNameAvailable(String name) {
        return categoryDAO.getCategoryByName(name) == null;
    }

    public boolean isNameAvailableForUpdate(int categoryId, String name) {
        Category c = categoryDAO.getCategoryByName(name);

        if (c == null) {
            return true;
        }

        return c.getCategoryId() == categoryId;
    }

    public Category registerCategory(String name){
        Category newCategory = new Category(0, name);
        return categoryDAO.create(newCategory);
    }

    public List<Category> getAllCategories(){
        return categoryDAO.getAll();
    }

    public Category getCategory(int categoryId){
        return categoryDAO.getById(categoryId);
    }

    public List<Product> getCategoryProducts(int categoryId){
        return productDAO.getProductsByCategory(categoryId);
    }


    public Category updateCategory(int categoryId,String name){
        Category categoryToUpdate = new Category(categoryId, name);
        return categoryDAO.update(categoryToUpdate);
    }

    public Category updateStatus(int categoryId, boolean status) {

        return categoryDAO.updateStatus(categoryId, status);
    }

}
