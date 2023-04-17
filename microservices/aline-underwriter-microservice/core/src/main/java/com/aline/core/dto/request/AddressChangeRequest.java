package com.aline.core.dto.request;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressChangeRequest {

    @Address
    private String address;

    private String city;

    private String state;

    @Zipcode
    private String zipcode;

}
