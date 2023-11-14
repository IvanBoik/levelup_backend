package com.boiko_ivan.spring.levelup_back.auth;


public record SignInRequest(
        String email,
        String password
) {

}
