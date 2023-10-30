package com.boiko_ivan.spring.levelup_back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidJwtException extends RuntimeException {

    public InvalidJwtException() {
        super();
    }

    public InvalidJwtException(Throwable e) {
        super(e);
    }

    public InvalidJwtException(String message) {
        super(message);
    }

    public InvalidJwtException(String message, Throwable e) {
        super(message, e);
    }
}
