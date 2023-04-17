package com.aline.core.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateDebitCardResponse {

    private String accountNumber;
    private String cardHolderId;
    private String cardHolder;
    private String cardNumber;
    private String securityCode;
    private LocalDate expirationDate;

}
