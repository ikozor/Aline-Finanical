package com.aline.core.annotation;

import com.aline.core.CoreModuleConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable entity and component scan in the
 * core module {@link com.aline.core}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CoreModuleConfiguration.class)
@Documented
public @interface EnableCoreModule {
}
