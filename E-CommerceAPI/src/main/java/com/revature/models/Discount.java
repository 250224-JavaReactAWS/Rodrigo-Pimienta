package com.revature.models;

import java.time.OffsetDateTime;

public class Discount {
    private int discountId;
    private String code;
    private Double percentage;
    private short maxUsesPerUser;
    private OffsetDateTime expiredAt;
    private boolean active;
    private int  remainUses;

    public Discount() {
    }

    public Discount(String code, Double percentage, short maxUsesPerUser, OffsetDateTime expiredAt) {
        this.percentage = percentage;
        this.code = code;
        this.maxUsesPerUser = maxUsesPerUser;
        this.expiredAt=expiredAt;
    }

    public Discount(int discountId, String code, Double percentage, short maxUsesPerUser, OffsetDateTime expiredAt, boolean active) {
        this.discountId = discountId;
        this.code = code;
        this.percentage = percentage;
        this.maxUsesPerUser = maxUsesPerUser;
        this.expiredAt = expiredAt;
        this.active = active;
    }

    public Discount(int discountId, String code, Double percentage, short maxUsesPerUser, OffsetDateTime expiredAt, boolean active, int remainUses) {
        this.discountId = discountId;
        this.code = code;
        this.percentage = percentage;
        this.maxUsesPerUser = maxUsesPerUser;
        this.expiredAt = expiredAt;
        this.active = active;
        this.remainUses = remainUses;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public short getMaxUsesPerUser() {
        return maxUsesPerUser;
    }

    public void setMaxUsesPerUser(short maxUsesPerUser) {
        this.maxUsesPerUser = maxUsesPerUser;
    }

    public OffsetDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(OffsetDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int  getRemainUses() {
        return remainUses;
    }

    public void setRemainUses(int remainUses) {
        this.remainUses = remainUses;
    }

}
