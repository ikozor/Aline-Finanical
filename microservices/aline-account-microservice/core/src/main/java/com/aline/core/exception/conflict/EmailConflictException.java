package com.aline.core.exception.conflict;

import com.aline.core.exception.ConflictException;

/**
 * Email conflict exception.
 * <p>
 *     <em>Throw this if a repository if an entity being saved shares an email with another entity of the same type.</em>
 * </p>
 * @see ConflictException
 */
public class EmailConflictException extends ConflictException {
    public EmailConflictException() {
        super("Email already exists.");
    }
}
