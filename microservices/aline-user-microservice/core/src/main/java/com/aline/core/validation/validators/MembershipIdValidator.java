package com.aline.core.validation.validators;


import com.aline.core.validation.annotation.MembershipId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MembershipIdValidator implements ConstraintValidator<MembershipId, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null)
            return true;

        return value.matches("^\\d{8}$");
    }
}
