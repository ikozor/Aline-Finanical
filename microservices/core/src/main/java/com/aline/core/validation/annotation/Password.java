package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
