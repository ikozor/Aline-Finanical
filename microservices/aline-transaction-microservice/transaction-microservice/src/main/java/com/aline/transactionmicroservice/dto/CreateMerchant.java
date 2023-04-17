package com.aline.transactionmicroservice.dto;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchant {

    @NotNull
    @Size(min = 4, max = 8)
    private String code;
    @NotNull
    @Size(max = 150)
    private String name;
    @Size(max = 255)
    private String description;
    @Address
    private String address;
    private String city;
    private String state;
    @Zipcode
    private String zipcode;

}
