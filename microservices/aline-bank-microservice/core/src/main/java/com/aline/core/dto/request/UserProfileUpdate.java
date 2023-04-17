package com.aline.core.dto.request;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.PhoneNumber;
import com.aline.core.validation.annotation.Username;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * DTO used to update profile information.
 * <br><strong><em>
 *     THis DTO only includes information the API will allow a member to update.
 * </em></strong>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdate implements Serializable {

    @Username
    private String username;
    @Email
    private String email;
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

}
