package com.aline.core.dto.request;

import com.aline.core.validation.annotation.DateOfBirth;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class ActivateCardRequest {

    @CreditCardNumber
    @NotBlank(message = "Card number is required.")
    private String cardNumber;

    @NotBlank(message = "Security code is required.")
    @Length(min = 3, max = 3)
    @Pattern(regexp = "\\d{3}", message = "Security code is not valid.")
    private String securityCode;

    @NotNull(message = "Expiration date is required.")
    private LocalDate expirationDate;

    @DateOfBirth(minAge = 18, message = "Age must be at least 18.")
    @NotNull(message = "Date of birth is required.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Last 4 of Social Security number is required.")
    @Size(min = 4, max = 4, message = "Only input the last four of a Social Security number.")
    private String lastFourOfSSN;



}
