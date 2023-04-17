package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.AddressValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>String must be a well-formed street address.</p>
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AddressValidator.class)
@Documented
public @interface Address {

    String message() default "Address is not valid.";
    Type type() default Type.DEFAULT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum Type {
        DEFAULT,
        MAILING
    }

}
