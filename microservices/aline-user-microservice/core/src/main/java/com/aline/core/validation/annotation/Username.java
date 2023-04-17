package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface Username {

    String message() default "Username must be between 6-20 characters and only include letters, numbers, underscores, and periods.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
