package com.aline.core.security;

import com.aline.core.annotation.ConditionalOnMissingWebSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@ConditionalOnMissingWebSecurity
@Slf4j(topic = "Web Security Checker")
@Component
public class WebSecurityChecker {
    @PostConstruct
    public void init() {
        log.warn("Web security is ENABLED but no implementation of the AbstractWebSecurityConfig was found. Create a configuration that is annotated with @WebSecurityConfiguration and extends AbstractWebSecurityConfig or set app.security.disable-web-security to true to disable this warning.");
    }
}
