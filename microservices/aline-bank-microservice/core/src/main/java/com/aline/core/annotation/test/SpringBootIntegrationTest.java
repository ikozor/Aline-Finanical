package com.aline.core.annotation.test;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use for MockMVC integration test
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public @interface SpringBootIntegrationTest {

    @AliasFor(annotation = SpringBootTest.class, attribute = "properties")
    String[] value() default {};

    @AliasFor(annotation = SpringBootTest.class, attribute = "value")
    String[] properties() default {};

}
