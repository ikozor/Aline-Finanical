package com.aline.core.annotation;

import com.aline.core.config.DisableSecurityConfig;
import com.aline.core.security.config.AbstractWebSecurityConfig;
import com.aline.core.security.config.JwtConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The WebSecurityConfiguration annotation specifies
 * a web security class. This is to be used with a
 * class that extends an {@link AbstractWebSecurityConfig}.
 *
 * @see AbstractWebSecurityConfig
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableGlobalMethodSecurity
@Import(JwtConfig.class)
@ConditionalOnMissingBean(DisableSecurityConfig.class)
@Documented
public @interface WebSecurityConfiguration {
    @AliasFor(annotation = EnableGlobalMethodSecurity.class, attribute = "prePostEnabled")
    boolean prePostEnabled() default true;
    @AliasFor(annotation = EnableGlobalMethodSecurity.class, attribute = "jsr250Enabled")
    boolean jsr50Enabled() default false;
    @AliasFor(annotation = EnableGlobalMethodSecurity.class, attribute = "proxyTargetClass")
    boolean proxyTargetClass() default false;
    @AliasFor(annotation = EnableGlobalMethodSecurity.class, attribute = "securedEnabled")
    boolean securedEnabled() default false;
}
