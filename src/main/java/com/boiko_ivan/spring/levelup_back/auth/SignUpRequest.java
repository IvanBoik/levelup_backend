package com.boiko_ivan.spring.levelup_back.auth;

public record SignUpRequest(
     String name,
     String surname,
     String email,
     String password
) {
}
