package com.aline.core.security.filter;

import com.aline.core.config.DisableSecurityConfig;
import com.aline.core.dto.request.AuthenticationRequest;
import com.aline.core.exception.ForbiddenException;
import com.aline.core.security.config.AbstractWebSecurityConfig;
import com.aline.core.security.config.JwtConfig;
import com.aline.core.security.model.UserAuthToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class provides the JWT Token to
 * any user that attempts to log in and
 * is successfully authenticated.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(DisableSecurityConfig.class)
@ConditionalOnBean(AbstractWebSecurityConfig.class)
public class JwtTokenProvider extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey jwtSecretKey;
    private final ObjectMapper objectMapper;

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            val authRequest = objectMapper.readValue(request.getInputStream(),
                    AuthenticationRequest.class);

            val authentication = new UserAuthToken(authRequest.getUsername(), authRequest.getPassword());

            return getAuthenticationManager().authenticate(authentication);

        } catch (IOException e) {
            throw new ForbiddenException("Unable to authenticate user.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        int expireAfterDays = jwtConfig.getTokenExpirationAfterDays();

        GrantedAuthority authority = new ArrayList<>(authResult.getAuthorities()).get(0);

        String token = jwtConfig.getTokenPrefix() + " " + Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authority", authority.getAuthority())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(expireAfterDays, ChronoUnit.DAYS)))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();

        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}
