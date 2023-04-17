package com.aline.core.exception.unprocessable;

import com.aline.core.exception.UnprocessableException;

public class ApplicantUnprocessableException extends UnprocessableException {
    public ApplicantUnprocessableException() {
        super("Applicant could not be processed.");
    }
}
