package com.aline.bankmicroservice.dto.request;

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
public class CreateBank {

    private String routingNumber;

    @Address(message = "'${validatedValue}' is not a valid address.")
    private String address;

    private String city;

    private String state;
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    private String zipcode;
}
