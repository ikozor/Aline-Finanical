package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

public class ApplicationNotFoundException extends NotFoundException {
    public ApplicationNotFoundException() {
        super("Application does not exists.");
    }
}
