package com.aline.core.security.model;

import com.aline.core.model.user.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class UserRoleAuthority implements GrantedAuthority {

    private final UserRole userRole;

    public UserRoleAuthority(String authority) {
        this.userRole = UserRole.valueOf(authority.toUpperCase());
    }

    @Override
    public String getAuthority() {
        return userRole.getRole();
    }
}
