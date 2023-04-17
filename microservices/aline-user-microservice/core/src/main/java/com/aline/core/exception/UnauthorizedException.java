package com.aline.core.exception;

/**
 * Status code 401 Unauthorized
 */
public class UnauthorizedException extends ResponseEntityException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
