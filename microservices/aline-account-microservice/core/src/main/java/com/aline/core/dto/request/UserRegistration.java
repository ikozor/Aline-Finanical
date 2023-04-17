package com.aline.core.dto.request;

import com.aline.core.validation.annotation.Password;
import com.aline.core.validation.annotation.Username;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * The UserRegistration abstract class is the
 * base class of any users with specific roles.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "role")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MemberUserRegistration.class, name = "member"),
        @JsonSubTypes.Type(value = AdminUserRegistration.class, name = "admin")
})
public abstract class UserRegistration implements Serializable {

    @NotBlank(message = "Username is required.")
    @Username
    private String username;

    @NotBlank(message = "Password is required.")
    @Password
    private String password;
}
