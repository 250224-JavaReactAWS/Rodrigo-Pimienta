package com.revature.repos;

import com.revature.models.UserAddress;

import java.util.List;

public interface UserAddressDAO extends  GeneralDAO<UserAddress>{
    List<UserAddress> getUserAddress(int userId);
    UserAddress updateStatus(int userId, boolean status);
}
