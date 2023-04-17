package com.aline.core.dto.request;

import com.aline.core.dto.response.ContactMethod;
import com.aline.core.validation.annotation.Username;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ResetPasswordAuthentication {

    /**
     * Username of the user requesting
     * a password reset
     */
    @Username
    private String username;

    /**
     * The contact method selected by the user.
     */
    @NotNull
    private ContactMethod contactMethod;

}
