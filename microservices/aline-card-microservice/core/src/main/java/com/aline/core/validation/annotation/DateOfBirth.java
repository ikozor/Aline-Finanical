package com.aline.core.validation.annotation;

import com.aline.core.validation.validators.DateOfBirthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

/**
 * <p>{@link LocalDate} must represent
 * a birth date that results in the age being at least
 * the minimum age provided by <code>minAge</code></code></p> property.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateOfBirthValidator.class)
@Documented
public @interface DateOfBirth {

    int minAge() default 0;

    String message() default "Date of birth does not meet the minimum age requirement.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
