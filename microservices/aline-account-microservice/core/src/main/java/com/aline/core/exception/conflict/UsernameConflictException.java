package com.aline.core.exception.conflict;

import com.aline.core.exception.ConflictException;

public class UsernameConflictException extends ConflictException {
    public UsernameConflictException() {
        super("Username already exists.");
    }
}
