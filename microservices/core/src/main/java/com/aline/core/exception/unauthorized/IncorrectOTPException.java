package com.aline.core.exception.unauthorized;

import com.aline.core.exception.UnauthorizedException;

/**
 * Used for the authentication of a One-Time Passcode.
 */
public class IncorrectOTPException extends UnauthorizedException {
    public IncorrectOTPException() {
        super("One-time passcode is not correct.");
    }
}
