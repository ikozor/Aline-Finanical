package com.aline.core.annotation;

import com.aline.core.config.DisableSecurityConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a conditional bean that is scanned when
 * web security is enabled.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ConditionalOnMissingBean(DisableSecurityConfig.class)
@Documented
public @interface ConditionalOnWebSecurityEnabled {
}
