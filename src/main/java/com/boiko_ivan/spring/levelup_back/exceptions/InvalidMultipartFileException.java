package com.boiko_ivan.spring.levelup_back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidMultipartFileException extends RuntimeException {

    public InvalidMultipartFileException() {
        super();
    }

    public InvalidMultipartFileException(Throwable e) {
        super(e);
    }

    public InvalidMultipartFileException(String message) {
        super(message);
    }

    public InvalidMultipartFileException(String message, Throwable e) {
        super(message, e);
    }
}
