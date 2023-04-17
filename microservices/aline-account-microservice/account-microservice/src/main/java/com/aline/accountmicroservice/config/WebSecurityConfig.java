package com.aline.accountmicroservice.config;

import com.aline.core.annotation.WebSecurityConfiguration;
import com.aline.core.security.config.AbstractWebSecurityConfig;
import com.aline.core.security.service.AbstractAuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@WebSecurityConfiguration
public class WebSecurityConfig extends AbstractWebSecurityConfig {
    @Override
    protected void configureHttp(HttpSecurity http) throws Exception {

    }
}
