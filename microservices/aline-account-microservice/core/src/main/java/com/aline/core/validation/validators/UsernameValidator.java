package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.Username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        return value.matches("^(?=.{6,20}$)(?![_.0-9])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    }
}
