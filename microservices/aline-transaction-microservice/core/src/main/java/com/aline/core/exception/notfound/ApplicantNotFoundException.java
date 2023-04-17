package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

public class ApplicantNotFoundException extends NotFoundException {
    public ApplicantNotFoundException() {
        super("Applicant does not exist.");
    }
}
