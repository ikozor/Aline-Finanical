package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The string has to be a name that contains only alphabetic letters, while also allowing hyphens and spaces.</p>
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameValidator.class)
@Documented
public @interface Name {

    String message() default "Name may only contain alphabetic letters as well as hyphens and spaces.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
