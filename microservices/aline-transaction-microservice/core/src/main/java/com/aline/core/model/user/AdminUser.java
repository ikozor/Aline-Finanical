package com.aline.core.model.user;

import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue(UserRole.Roles.ADMINISTRATOR)
public class AdminUser extends User {

    @Name
    @NotNull
    private String firstName;

    @Name
    @NotNull
    private String lastName;

    @NotNull
    @Email(message = "'${validatedValue}' is not a valid email.")
    private String email;

    @NotNull
    @PhoneNumber
    private String phone;

    @PrePersist
    public void postPersist() {
        if (getId() == 0) {
            setEnabled(true);
        }
    }
}
