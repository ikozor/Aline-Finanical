package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.ZipcodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * String must be a ZIP code in 5 digit or +4 format.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZipcodeValidator.class)
@Documented
public @interface Zipcode {
    String message() default "'${validatedValue}' is not a valid zipcode.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
