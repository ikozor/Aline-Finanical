package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.DateOfBirth;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, LocalDate> {

    int minAge = 0;

    @Override
    public void initialize(DateOfBirth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minAge = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age >= minAge;
    }
}
