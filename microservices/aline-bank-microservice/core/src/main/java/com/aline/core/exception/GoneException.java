package com.aline.core.exception;

/**
 * GoneException represents status code 410 which represents
 * a resource that can no longer be accessed.
 */
public class GoneException extends ResponseEntityException {
    public GoneException(String message) {
        super(message);
    }
}
