package com.aline.core.security.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

@Getter
@Builder
public class UserAuthToken extends UsernamePasswordAuthenticationToken {

    private String username;
    private String password;
    private GrantedAuthority authority;

    public UserAuthToken(String username, String password) {
        super(username, password);
        this.username = username;
        this.password = password;
        this.authority = null;
    }

    public UserAuthToken(String username, String password, GrantedAuthority authority) {
        super(username, password,
                authority == null ? Collections.emptySet() :
                Collections.singleton(authority));
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public UserAuthToken(String username, GrantedAuthority authority) {
        super(username, null, authority == null ? Collections.emptySet() :
                Collections.singleton(authority));
        this.username = username;
        this.password = null;
        this.authority = authority;
    }
}
