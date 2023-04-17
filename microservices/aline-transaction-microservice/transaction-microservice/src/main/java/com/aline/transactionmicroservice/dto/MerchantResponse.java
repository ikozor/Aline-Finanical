package com.aline.transactionmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Merchant response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantResponse {

    private String code;
    private String name;
    private String description;
    private LocalDateTime registeredAt;

}
