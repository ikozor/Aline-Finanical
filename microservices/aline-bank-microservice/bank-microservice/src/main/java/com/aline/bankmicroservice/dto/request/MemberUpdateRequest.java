package com.aline.bankmicroservice.dto.request;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.PhoneNumber;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequest {
    @Email
    private String email;
    @Name
    private String firstName;

    private String middleName;

    @Name
    private String lastName;
    @PhoneNumber
    private String phone;
    private String driversLicense;
    private int income;

    @Address
    private String address;
    private String city;
    private String state;
    @Zipcode
    private String zipcode;

    @Address
    private String mailingAddress;
    private String mailingCity;
    private String mailingState;
    @Zipcode
    private String mailingZipcode;

    private String membershipId;
}
