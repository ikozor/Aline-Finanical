package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.Zipcode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZipcodeValidator implements ConstraintValidator<Zipcode, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("^\\d{5}(-\\d{4})?$");
    }
}
