package com.revature.services;

import com.revature.config.TimeZoneConfig;
import com.revature.models.Discount;
import com.revature.repos.DiscountDAO;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public class DiscountService {
    private final DiscountDAO discountDAO;

    public DiscountService(DiscountDAO discountDAO){
        this.discountDAO=discountDAO;
    }

    public Discount registerDiscount(String code, Double percentage, short maxUsesPerUser, OffsetDateTime expiredAt){
        Discount newDiscount = new Discount(code, percentage,maxUsesPerUser, expiredAt);
        return discountDAO.create(newDiscount);
    }

    // validate resetCode (Security)
    public boolean validateDiscount(String resetCode){
        /*
            Validations
            Length ==10
            At least 1 lower case character
            At least 1 Upper case character
            At least 1 digit
         */

        if(resetCode == null || resetCode.length() != 10){
            return false;
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;

        for(char c: resetCode.toCharArray()){
            if(Character.isLowerCase(c)){
                hasLower = true;
            }else if(Character.isUpperCase(c)){
                hasUpper = true;
            } else if(Character.isDigit(c)){
                hasDigit = true;
            }
        }

        return hasLower && hasUpper && hasDigit;
    }

    // validate resetCode( Unique)
    public boolean isDiscountExist(String resetCode){
        Discount returnedDiscount = discountDAO.getDiscountByCode(resetCode);

        return returnedDiscount == null;
    }

    // validate resetCode (Availability)
    public boolean isDiscountAvailable(String resetCode, int userId){
        /*
         Validations
         Check if the reset code exists
         Check if the code is active
         Check if it's expired
         Check if exists remain uses
         */
        Discount returnedDiscount = discountDAO.getCodeByUserAndCode(resetCode, userId);

        if(returnedDiscount == null){
            return false;
        }

        if(!returnedDiscount.isActive()){
            return false;
        }

        OffsetDateTime expiredAt = returnedDiscount.getExpiredAt();
        ZoneId zoneId = ZoneId.of(TimeZoneConfig.ZONE_ID);
        OffsetDateTime now = OffsetDateTime.now(zoneId);

        if(!expiredAt.isAfter(now)){
            return false;
        }

        return returnedDiscount.getRemainUses() > 0;
    }

    public List<Discount> getAllDiscounts(){
        return discountDAO.getAll();
    }

    public List<Discount> getAvailableUserDiscounts(int userId){
        return discountDAO.getAvailableDiscountsByUser(userId);
    }

    public Discount getDiscount(int discountId){
        return  discountDAO.getById(discountId);
    }

    public Discount getDiscountByCode(String code){
        return  discountDAO.getDiscountByCode(code);
    }

    public Discount updateDiscount(int discountId, Double percentage, short maxUsesPerUser, OffsetDateTime expiredAt ){
        Discount newDiscount = new Discount(discountId, null, percentage,maxUsesPerUser, expiredAt, true);
        return discountDAO.update(newDiscount);
    }

    public Discount updateStatus(int discountId, boolean status) {

        return discountDAO.updateStatus(discountId, status);
    }

}
