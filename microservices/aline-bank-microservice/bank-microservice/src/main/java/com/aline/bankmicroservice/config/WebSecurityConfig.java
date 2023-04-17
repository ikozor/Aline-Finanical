package com.aline.bankmicroservice.config;

import com.aline.core.annotation.WebSecurityConfiguration;
import com.aline.core.security.config.AbstractWebSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@WebSecurityConfiguration
@Slf4j(topic = "Web Security - Bank")
public class WebSecurityConfig extends AbstractWebSecurityConfig {

    @Override
    protected void configureHttp(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/branches", "/banks/**")
                .permitAll();
    }
}
