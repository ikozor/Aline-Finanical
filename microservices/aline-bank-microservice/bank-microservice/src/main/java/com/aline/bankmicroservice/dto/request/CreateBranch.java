package com.aline.bankmicroservice.dto.request;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.PhoneNumber;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBranch {

    private String name;

    @Address
    private String address;

    private String city;

    private String state;

    @Zipcode
    private String zipcode;

    @PhoneNumber
    private String phone;

    private Long bankID;

}
