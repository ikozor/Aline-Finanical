package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.SocialSecurity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SocialSecurityValidator implements ConstraintValidator<SocialSecurity, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("^\\d{3}[.\\s-]\\d{2}[.\\s-]\\d{4}$");
    }
}
