package com.aline.core.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Disables web security
 */
@Configuration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@ConditionalOnProperty(
        prefix = "app.security",
        name = "disable-web-security",
        havingValue = "true"
)
public class DisableSecurityConfig {
}
