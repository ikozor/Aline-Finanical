package com.aline.core.dto.request;

import com.aline.core.validation.annotation.Username;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * One-Time Password authentication
 */
@Data
@Builder
public class OtpAuthentication {
    @Username
    private String username;

    @Size(min = 6, max = 6)
    private String otp;
}
