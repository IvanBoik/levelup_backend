package com.boiko_ivan.spring.levelup_back.exceptions;

public class RefreshTokenExpiredException extends RuntimeException {

    public RefreshTokenExpiredException() {
        super();
    }

    public RefreshTokenExpiredException(Throwable e) {
        super(e);
    }

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

    public RefreshTokenExpiredException(String message, Throwable e) {
        super(message, e);
    }
}
