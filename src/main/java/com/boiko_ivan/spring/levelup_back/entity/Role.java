package com.boiko_ivan.spring.levelup_back.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER("user"),
    ADMIN("admin");

    private final String authority;

    Role (String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority () {
        return authority;
    }
}
