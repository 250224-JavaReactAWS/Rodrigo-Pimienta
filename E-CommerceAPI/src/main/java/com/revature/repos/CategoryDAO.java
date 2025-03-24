package com.revature.repos;

import com.revature.models.Category;

public interface CategoryDAO extends  GeneralDAO<Category> {
    Category getCategoryByName(String name);
    Category updateStatus(int categoryId, boolean status);
}
