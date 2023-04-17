package com.aline.core.security.filter;

import com.aline.core.config.DisableSecurityConfig;
import com.aline.core.exception.UnauthorizedException;
import com.aline.core.security.config.JwtConfig;
import com.aline.core.security.model.JwtToken;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The JwtRequestFilter is an abstract class
 * that verifies the JWT token and provides and
 * filters based on the validity of the token.
 * Extra logic may be implemented that is based on the
 * token.
 */
@Component
@ConditionalOnMissingBean(DisableSecurityConfig.class)
public abstract class JwtTokenFilter extends OncePerRequestFilter {

    private JwtConfig jwtConfig;
    private SecretKey jwtSecretKey;

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Autowired
    public void setJwtSecretKey(SecretKey jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    protected abstract void doFilterWithJwt(JwtToken token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("");

        // Validate authorization header
        if (authHeader.isEmpty() || !authHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(jwtConfig.getTokenPrefix().length()).trim();

        try {
            JwtToken jwtToken = JwtToken.from(token, jwtSecretKey);

            // Check token expiration
            if (jwtToken.isExpired()) {
                filterChain.doFilter(request, response);
                return;
            }

            doFilterWithJwt(jwtToken, request, response, filterChain);
        } catch (JwtException e) {
            e.printStackTrace();
            throw new UnauthorizedException("The provided token cannot be trusted.");
        }
        filterChain.doFilter(request, response);
    }

}
