package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("^(\\+\\d[\\s-])?\\(?\\d{3}\\)?[\\s-]\\d{3}[\\s-]\\d{4}$");
    }
}
