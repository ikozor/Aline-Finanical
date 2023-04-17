package com.aline.core.exception;

/**
 * Bad Gateway Exception is thrown at error code 502.
 * Something failed with an upstream operation.
 */
public class BadGatewayException extends ResponseEntityException {
    public BadGatewayException(String message) {
        super(message);
    }
}
