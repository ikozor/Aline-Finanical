package com.aline.core.exception.unprocessable;

import com.aline.core.exception.UnprocessableException;

public class ApplicationUnprocessableException extends UnprocessableException {
    public ApplicationUnprocessableException() {
        super("Application could not be processed.");
    }
}
