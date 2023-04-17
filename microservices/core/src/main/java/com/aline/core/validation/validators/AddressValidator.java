package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.Address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AddressValidator implements ConstraintValidator<Address, String> {

    Address.Type type;

    @Override
    public void initialize(Address constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Pattern pattern = Pattern.compile(type.equals(Address.Type.DEFAULT) ?
                "^([0-9]+([a-zA-Z]+)?)\\s(.*)(\\s)([a-zA-Z]+)(\\.)?(\\s(#?(\\w+))|([A-Za-z]+\\.?(\\w+)))?$" :
                "^(((PO|P O|P.O)\\.?\\s(Box)\\s([0-9]+))|(([0-9]+([a-zA-Z]+)?)\\s(.*)(\\s)([a-zA-Z]+)(\\.)?(\\s(#?(\\w+))|([A-Za-z]+\\.?(\\w+)))?))$");

        if (value == null) {
            return true;
        }
        return value.matches(pattern.pattern());
    }
}
