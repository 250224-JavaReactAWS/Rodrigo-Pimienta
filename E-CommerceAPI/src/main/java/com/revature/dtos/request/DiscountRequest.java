package com.revature.dtos.request;

public record DiscountRequest (Double percentage,short maxUsesPerUser, String expiredAt) {
}
