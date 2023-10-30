package com.boiko_ivan.spring.levelup_back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidS3ObjectException extends RuntimeException {

    public InvalidS3ObjectException() {
        super();
    }

    public InvalidS3ObjectException(Throwable e) {
        super(e);
    }

    public InvalidS3ObjectException(String message) {
        super(message);
    }

    public InvalidS3ObjectException(String message, Throwable e) {
        super(message, e);
    }
}
