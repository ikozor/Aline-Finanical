package com.aline.core.exception.gone;

import com.aline.core.exception.GoneException;

/**
 * Token no longer exists or is expired."
 */
public class TokenExpiredException extends GoneException {
    public TokenExpiredException() {
        super("Token has expired or no longer exists.");
    }
}
