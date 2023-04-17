package com.aline.core.validation.annotation;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ensures the membership number is only 10 digits long.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MembershipId {

    String message() default "'${validatedValue}' is not a valid membership number.";

    Class<?>[] groups() default  {};

    Class<? extends Payload>[] payload() default {};

}
