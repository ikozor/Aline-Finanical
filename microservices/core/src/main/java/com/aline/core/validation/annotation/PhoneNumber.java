package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>String must be a well-formed phone number.</p>
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {
    String message() default "'${validatedValue}' is not a valid phone number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
