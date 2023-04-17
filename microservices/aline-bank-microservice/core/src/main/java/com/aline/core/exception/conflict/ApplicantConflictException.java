package com.aline.core.exception.conflict;

import com.aline.core.exception.ConflictException;

/**
 * Used to abstract other conflict exceptions that may not need
 * to be specific when a request with conflicting information
 * is made.
 * <p>
 *     <em><strong>Example: </strong>Email already exists -> {@link ApplicantConflictException} "Applicant already exists."</em>
 * </p>
 */
public class ApplicantConflictException extends ConflictException {
    public ApplicantConflictException() {
        super("Applicant with personal identifying information already exists.");
    }
}
