package com.revature.controllers;

import com.revature.dtos.request.StatusUpdateRequest;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Category;
import com.revature.models.Product;
import com.revature.models.UserRole;
import com.revature.services.CategoryService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService=categoryService;
    }

    public void getAllCategoriesHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged in to view this method!"));
            return;
        }
        ctx.json(categoryService.getAllCategories());
    }

    public void getCategoryHandler(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String userIdFormPath = ctx.pathParam("id");

        if(userIdFormPath == null || userIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category ID is required in the path."));
            return;
        }

        int categoryId;
        try{
            categoryId = Integer.parseInt(userIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Category ID format. Must be a number."));
            return;
        }

        Category category = categoryService.getCategory(categoryId);

        if(category == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        ctx.status(200);
        ctx.json(category);
    }

    public void getCategoryProducts(Context ctx){
        if(ctx.sessionAttribute("userId") == null){
            ctx.status(400);
            ctx.json(new ErrorMessage("Yoy must be logged in to view this method!"));
            return;
        }

        String userIdFormPath = ctx.pathParam("id");

        if(userIdFormPath == null || userIdFormPath.isEmpty()){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category ID is required in the path."));
            return;
        }

        int categoryId;
        try{
            categoryId = Integer.parseInt(userIdFormPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Category ID format. Must be a number."));
            return;
        }

        List<Product> products = categoryService.getCategoryProducts(categoryId);

        if(products == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        ctx.status(200);
        ctx.json(products);
    }

    public void registerCategoryHandler(Context ctx){
        Category requestCategory = ctx.bodyAsClass(Category.class);

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

        if(!categoryService.isNameAvailable(requestCategory.getName())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category name is not available, please select a new one"));
            logger.warn("Register attempt made for taken category name: "+ requestCategory.getName());
            return;
        }

        Category registerCategory = categoryService.registerCategory(
                requestCategory.getName()
        );

        if(registerCategory == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New category registered with name: "+requestCategory.getName());

        ctx.status(201);
        ctx.json(registerCategory);
    }

    public void updateCategoryHandler(Context ctx){
        Category requestCategory = ctx.bodyAsClass(Category.class);

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

        String CategoryIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (CategoryIdFromPath == null || CategoryIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Category ID is required in the path."));
            return;
        }

        int categoryId;
        try {
            categoryId = Integer.parseInt(CategoryIdFromPath); // Parse userId as an integer
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Category ID format. Must be a number."));
            return;
        }

        if(!categoryService.isNameAvailableForUpdate(categoryId, requestCategory.getName())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Category name is not available, please select a new one"));
            logger.warn("Update attempt made for taken category name: "+ requestCategory.getName());
            return;
        }

        Category registedCategory = categoryService.updateCategory(
                categoryId,
                requestCategory.getName()
        );

        if(registedCategory == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Category updated with categoryId: "+categoryId);

        ctx.status(200);
        ctx.json(registedCategory);
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

        String categoryIdFromPath = ctx.pathParam("id"); // Extract userId from path

        if (categoryIdFromPath == null || categoryIdFromPath.isEmpty()) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Category ID is required in the path."));
            return;
        }

        int categoryId;
        try{
            categoryId = Integer.parseInt(categoryIdFromPath);
        }catch (NumberFormatException e){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Category ID format. Must be a number."));
            return;
        }

        StatusUpdateRequest request = ctx.bodyAsClass(StatusUpdateRequest.class);
        Category registeredCategory = categoryService.updateStatus(
                categoryId,
                request.status()
        );

        if(registeredCategory == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("Category updated with category ID: "+categoryId);

        ctx.status(200);
        ctx.json(registeredCategory);
    }

}
