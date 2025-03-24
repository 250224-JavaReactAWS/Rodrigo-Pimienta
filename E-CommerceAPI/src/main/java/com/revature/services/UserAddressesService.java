package com.revature.services;

import com.revature.models.UserAddress;
import com.revature.repos.UserAddressDAO;


public class UserAddressesService {

    private final UserAddressDAO userAddressDAO;

    public UserAddressesService(UserAddressDAO userAddressDAO){
        this.userAddressDAO = userAddressDAO;
    }


    public UserAddress registerNewAddress(int userId, String country, String state, String city, String street, String house_number, String postal_code){
        UserAddress newUserAddress =new UserAddress(0, userId, country, state, city, street,house_number, postal_code);
        return userAddressDAO.create(newUserAddress);
    }

    public UserAddress getUserAddress(int userAddressId){
        return userAddressDAO.getById(userAddressId);
    }

    public UserAddress updateAddress(int userAddressId,int userId, String country, String state, String city, String street, String house_number, String postal_code){
        UserAddress newUserAddress =new UserAddress(userAddressId, userId, country, state, city, street,house_number, postal_code);
        return userAddressDAO.update(newUserAddress);
    }

    public UserAddress updateStatus(int userId, boolean status) {
        return userAddressDAO.updateStatus(userId, status);
    }
}
