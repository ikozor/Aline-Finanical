package com.aline.core.exception;

/**
 * 403 Forbidden
 */
public class ForbiddenException extends ResponseEntityException {
    public ForbiddenException(String message) {
        super(message);
    }
}
