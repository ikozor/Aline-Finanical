package com.aline.core.dto.response;

import com.aline.core.validation.annotation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo {
    @PhoneNumber
    private String phone;
    @Email
    private String email;
}
