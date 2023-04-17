package com.aline.core.dto.request;

import com.aline.core.validation.annotation.Password;
import com.aline.core.validation.annotation.Username;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;

/**
 * DTO to send reset password requests.
 */
@Data
@Builder
public class ResetPasswordRequest {
    /**
     * The one-time passcode to allow a user
     * to reset their password.
     */
    @Size(min = 6, max = 6)
    @Length(min = 6, max = 6)
    private String otp;

    /**
     * The username of the user requesting
     * to reset their password.
     */
    @Username
    private String username;

    /**
     * The new password.
     */
    @Password
    private String newPassword;
}
