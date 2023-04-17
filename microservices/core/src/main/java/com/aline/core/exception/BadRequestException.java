package com.aline.core.exception;

/**
 * Super class for bad request exceptions.
 * <p>Status Code: <code>400 BAD REQUEST</code></p>
 * <p>
 *     <em>Extends <code>{@link RuntimeException}</code> to allow it to be caught by the {@link GlobalExceptionHandler}.</em>
 * </p>
 */
public class BadRequestException extends ResponseEntityException {
    public BadRequestException(String message) {
        super(message);
    }
}
