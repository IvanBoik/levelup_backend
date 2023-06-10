package com.boiko_ivan.spring.levelup_back.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    USER("user"),
    ADMIN("admin");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
