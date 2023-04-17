package com.aline.core.security.service;

import com.aline.core.config.DisableSecurityConfig;
import com.aline.core.exception.UnauthorizedException;
import com.aline.core.model.user.User;
import com.aline.core.model.user.UserRole;
import com.aline.core.repository.UserRepository;
import com.aline.core.security.model.SecurityUser;
import com.aline.core.security.model.UserRoleAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(DisableSecurityConfig.class)
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found.", username)));

        UserRole role = Optional.of(user.getUserRole())
                .orElseThrow(() -> new UnauthorizedException(String.format("User '%s' does not have the right permissions.", username)));

        UserRoleAuthority authority = new UserRoleAuthority(role);

        return SecurityUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authority(authority)
                .isEnabled(user.isEnabled())
                .build();
    }

}
