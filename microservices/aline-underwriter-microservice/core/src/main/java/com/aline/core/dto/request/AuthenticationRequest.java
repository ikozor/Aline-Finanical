package com.aline.core.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * DTO used for login authentication.
 */
@Data
@Builder
public class AuthenticationRequest {
    private String username;
    private String password;
}
