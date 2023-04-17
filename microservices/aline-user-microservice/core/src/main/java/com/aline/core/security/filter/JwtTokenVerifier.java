package com.aline.core.security.filter;

import com.aline.core.security.config.JwtConfig;
import com.aline.core.security.model.JwtToken;
import com.aline.core.security.model.UserAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(JwtConfig.class)
public class JwtTokenVerifier extends JwtTokenFilter {

    @Override
    protected void doFilterWithJwt(JwtToken token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {
        UserAuthToken authenticationToken = new UserAuthToken(token.getUsername(), token.getAuthority());
        val securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationToken);
    }
}
