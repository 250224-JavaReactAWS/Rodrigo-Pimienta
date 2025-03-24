package com.revature.models;

import java.time.OffsetDateTime;

public class UserResetCode {

    private int resetCodeId;
    private int userId;
    private String resetCode;
    private boolean used;
    private OffsetDateTime expiredAt;

    public UserResetCode() {
    }

    public UserResetCode(int resetCodeId, int userId) {
        this.resetCodeId = resetCodeId;
        this.userId = userId;
    }


    public UserResetCode(int resetCodeId, int userId, String resetCode, boolean used, OffsetDateTime expiredAt) {
        this.resetCodeId = resetCodeId;
        this.userId = userId;
        this.resetCode = resetCode;
        this.used=used;
        this.expiredAt = expiredAt;
    }

    public UserResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResetCodeId() {
        return resetCodeId;
    }

    public void setResetCodeId(int resetCodeId) {
        this.resetCodeId = resetCodeId;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public OffsetDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(OffsetDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}
