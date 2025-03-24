package com.revature.repos;

import com.revature.models.UserResetCode;

public interface UserResetCodeDAO extends GeneralDAO<UserResetCode> {
    UserResetCode getResetCodeByCode(String code);
    UserResetCode getActiveResetCodeByUser(int userId);
}


