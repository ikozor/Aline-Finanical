package com.aline.core.exception;

public class UnprocessableException extends ResponseEntityException {
    public UnprocessableException(String message) {
        super(message);
    }
}
