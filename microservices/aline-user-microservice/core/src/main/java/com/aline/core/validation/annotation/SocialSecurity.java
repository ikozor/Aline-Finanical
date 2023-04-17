package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.SocialSecurityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * String must be a well-formed Social Security number.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SocialSecurityValidator.class)
@Documented
public @interface SocialSecurity {

    String message() default "${validatedValue} is not a valid Social Security number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
