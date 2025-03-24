package com.revature.repos;

import com.revature.models.User;

public interface UserDAO extends  GeneralDAO<User> {

    /*
     Here I inherited all the methods of the GeneralDAO, and
     also I can create specific methods beyond the usual CRUD
     */

    User getUserByEmail(String email);
//    List<Order> getOrdersByUser(int userId);
//    List<Discount> getDiscountAvailable(int userId);
    User updateStatus(int userId, boolean status);
    User updatePassword(int userId, String password);
}
