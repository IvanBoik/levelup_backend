package com.boiko_ivan.spring.levelup_back.dto;

public record UserDTO(
        long id,
        String email,
        String name,
        String surname,
        String avatarURL
) {
}
