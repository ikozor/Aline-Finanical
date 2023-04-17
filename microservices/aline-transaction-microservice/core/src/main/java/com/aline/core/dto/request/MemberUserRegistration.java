package com.aline.core.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Registers a user with role MEMBER.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonTypeName("member")
@AllArgsConstructor
@NoArgsConstructor
public class MemberUserRegistration extends UserRegistration implements Serializable {

    @NotNull(message = "Membership ID is required.")
    private String membershipId;

    @NotNull(message = "Last 4 of Social Security number is required.")
    @Size(min = 4, max = 4, message = "Only input the last four of a Social Security number.")
    private String lastFourOfSSN;

}
