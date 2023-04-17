package com.aline.core.dto.response;

import com.aline.core.validation.annotation.MembershipId;
import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.Username;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Username
    private String username;
    @Name
    private String firstName;
    @Name
    private String middleName;
    @Name
    private String lastName;
    @MembershipId
    private String membershipId;
    private int income;
    private ContactInfo contactInfo;
    private AddressResponse billingAddress;
    private AddressResponse mailingAddress;

}
