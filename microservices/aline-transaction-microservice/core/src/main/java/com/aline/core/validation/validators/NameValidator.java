package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.Name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("^[A-Za-z][A-Za-z\\-\\s]+$");
    }
}
