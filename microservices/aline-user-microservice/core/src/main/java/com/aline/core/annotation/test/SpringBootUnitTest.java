package com.aline.core.annotation.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use for simple spring boot unit test
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@SpringBootTest
public @interface SpringBootUnitTest {
    @AliasFor(annotation = SpringBootTest.class, attribute = "properties")
    String[] value() default {};

    @AliasFor(annotation = SpringBootTest.class, attribute = "value")
    String[] properties() default {};
}
