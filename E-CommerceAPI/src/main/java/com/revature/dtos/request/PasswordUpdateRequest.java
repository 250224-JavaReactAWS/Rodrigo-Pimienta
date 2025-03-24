package com.revature.dtos.request;

public record PasswordUpdateRequest(String resetCode, String password) {
}
