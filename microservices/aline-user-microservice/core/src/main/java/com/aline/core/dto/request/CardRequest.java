package com.aline.core.dto.request;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class CardRequest {
    @NotBlank
    @CreditCardNumber
    private final String cardNumber;
    @NotBlank
    @Length(min = 3, max = 3)
    @Pattern(regexp = "\\d{3}")
    private final String securityCode;
    @NotNull
    private final LocalDate expirationDate;
}
