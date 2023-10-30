package com.boiko_ivan.spring.levelup_back.dto;

public record UpdateUserPasswordRequest(
        String email,
        String oldPassword,
        String newPassword
) {
}
