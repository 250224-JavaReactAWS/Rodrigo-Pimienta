package com.revature.services;

import com.revature.config.TimeZoneConfig;
import com.revature.models.UserResetCode;
import com.revature.repos.UserResetCodeDAO;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class UserResetCodeService {
    private final UserResetCodeDAO userResetCodeDAO;

    public UserResetCodeService(UserResetCodeDAO userResetCodeDAO){
        this.userResetCodeDAO=userResetCodeDAO;
    }

    // validate resetCode (Security)
    public boolean validateResetCode(String resetCode){
        /*
            Validations
            Length ==10
            At least 1 lower case character
            At least 1 Upper case character
            At least 1 digit
            At least 1 Special char of the pull (!"#$%&)
         */

        if(resetCode == null || resetCode.length() != 10){
            return false;
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;
        boolean hasDigit = false;

        String specialChars = "!\"#$%&";

        for(char c: resetCode.toCharArray()){
            if(Character.isLowerCase(c)){
                hasLower = true;
            }else if(Character.isUpperCase(c)){
                hasUpper = true;
            } else if(Character.isDigit(c)){
                hasDigit = true;
            }else if( specialChars.indexOf(c) != -1){
                hasSpecial = true;
            }
        }

        return hasLower && hasUpper && hasSpecial && hasDigit;
    }

    // validate resetCode( Unique)
    public boolean isResetCodeExist(String resetCode){
        UserResetCode returnedResetCode = userResetCodeDAO.getResetCodeByCode(resetCode);

        return returnedResetCode == null;
    }

    // validate resetCode (Availability)
    public boolean isResetCodeAvailable(String resetCode){
        /*
         Validations
         Check if the reset code exists
         Check if the code was already used
         Check if it's expired
         */
        UserResetCode returnedResetCode = userResetCodeDAO.getResetCodeByCode(resetCode);

        if(returnedResetCode == null){
            return false;
        }

        if(returnedResetCode.isUsed()){
            return false;
        }

        OffsetDateTime expiredAt = returnedResetCode.getExpiredAt();
        ZoneId zoneId = ZoneId.of(TimeZoneConfig.ZONE_ID);
        OffsetDateTime now = OffsetDateTime.now(zoneId);

        return expiredAt.isAfter(now);
    }

    public UserResetCode registerNewResetCode(int userId, String resetCode){
        ZoneId zoneId = ZoneId.of(TimeZoneConfig.ZONE_ID); // Get the ZoneId
        OffsetDateTime expiredAt = OffsetDateTime.now(zoneId).plusMinutes(5);
        UserResetCode newResetCode = new UserResetCode(0,userId, resetCode,false, expiredAt);
        return userResetCodeDAO.create(newResetCode);
    }

    public UserResetCode getResetCodeByCode(String resetCode){
        return userResetCodeDAO.getResetCodeByCode(resetCode);
    }

    public UserResetCode getActiveUserResetCode(int userId){
        return userResetCodeDAO.getActiveResetCodeByUser(userId);
    }

    public UserResetCode updateResetCode(int resetCodeId, int userId){
        UserResetCode userResetCodeToUpdate = new UserResetCode(resetCodeId, userId);
        return userResetCodeDAO.update(userResetCodeToUpdate);
    }
}
