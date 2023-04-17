package com.aline.core.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CardResponse {

    private String cardNumber;
    private String securityCode;
    private LocalDate expirationDate;
    private String cardHolder;
    private String cardStatus;

}
