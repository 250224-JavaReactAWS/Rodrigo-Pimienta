package com.revature.repos;

import com.revature.models.Discount;

import java.util.List;

public interface DiscountDAO extends GeneralDAO<Discount> {
    Discount getCodeByUserAndCode(String code, int userId);
    Discount getDiscountByCode(String code);
    Discount updateStatus(int discountId, boolean status);
    List<Discount> getAvailableDiscountsByUser(int userId);
}
