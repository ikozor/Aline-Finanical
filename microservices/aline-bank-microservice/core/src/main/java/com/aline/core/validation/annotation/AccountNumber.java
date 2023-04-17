package com.aline.core.validation.annotation;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Account number must be between 8 and 12 digits long.
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccountNumber {

    String message() default "'${validatedValue}' is not a valid account number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
