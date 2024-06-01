package com.app.meetly.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class OperatorNotFoundException extends Exception {

    public OperatorNotFoundException() {
        super("operator does not exist");
    }
}
