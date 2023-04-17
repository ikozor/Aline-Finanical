package com.aline.core.dto.request;

import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Registers a user with role ADMINISTRATOR.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonTypeName("admin")
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserRegistration extends UserRegistration implements Serializable {

    @Name
    @NotBlank(message = "First name is required.")
    private String firstName;

    @Name
    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email
    @NotBlank(message = "Email is required.")
    private String email;

    @PhoneNumber
    @NotBlank(message = "Phone number is required.")
    private String phone;

}
