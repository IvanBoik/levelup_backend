package com.boiko_ivan.spring.levelup_back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super();
    }

    public DuplicateEmailException(Throwable e) {
        super(e);
    }

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(Throwable e, String message) {
        super(message, e);
    }
}
