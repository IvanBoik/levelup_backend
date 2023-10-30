package com.boiko_ivan.spring.levelup_back.dto;

public record UpdateUserDataRequest(
        long id,
        String newEmail,
        String newName,
        String newSurname
) {
}
