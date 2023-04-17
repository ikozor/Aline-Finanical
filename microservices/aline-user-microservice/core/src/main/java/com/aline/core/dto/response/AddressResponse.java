package com.aline.core.dto.response;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    @Address
    private String address;
    private String city;
    private String state;
    @Zipcode
    private String zipcode;

}
