package com.aline.core.exception.conflict;

import com.aline.core.exception.ConflictException;

/**
 * Phone number conflict exception.
 * <p>
 *     <em>Throw this if a repository if an entity being saved shares a phone number with another entity of the same type.</em>
 * </p>
 * @see ConflictException
 */
public class PhoneConflictException extends ConflictException {
    public PhoneConflictException() {
        super("Phone number already exists.");
    }
}
