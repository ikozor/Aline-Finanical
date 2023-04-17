package com.aline.core.security.config;

import com.aline.core.config.DisableSecurityConfig;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * JWT Configuration class is used to inject
 * variable fields such as secret key into a
 * requesting bean.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.security.jwt")
@ConditionalOnMissingBean(DisableSecurityConfig.class)
public class JwtConfig {

    /**
     * JWT Secret Key
     */
    private String secretKey ;
    /**
     * Token prefix
     */
    private String tokenPrefix = "Bearer";
    /**
     * The number of days the token will expire after
     */
    private int tokenExpirationAfterDays = 14;

    // JWT Secret Key bean
    @Bean(name = "jwtSecretKey")
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

}
