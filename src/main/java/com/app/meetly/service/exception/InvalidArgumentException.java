package com.app.meetly.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String msg) {
        super(msg);
    }

}
