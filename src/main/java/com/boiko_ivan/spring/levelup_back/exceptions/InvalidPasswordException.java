package com.boiko_ivan.spring.levelup_back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(Throwable e) {
        super(e);
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable e) {
        super(message, e);
    }
}
