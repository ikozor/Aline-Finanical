package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User does not exist.");
    }
}
