package com.aline.core.dto.response;

import com.aline.core.model.user.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Response DTO for a payload for user information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UserRole role;
    private boolean enabled;

    // Properties only shown in an authenticated user response
    private Long memberId;
    private String membershipId;

}
