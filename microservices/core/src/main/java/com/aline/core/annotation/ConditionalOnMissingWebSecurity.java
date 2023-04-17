package com.aline.core.annotation;

import com.aline.core.security.config.AbstractWebSecurityConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a bean that is missing a web
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ConditionalOnMissingBean(AbstractWebSecurityConfig.class)
@ConditionalOnWebSecurityEnabled
@Documented
public @interface ConditionalOnMissingWebSecurity {
}
