package com.aline.core.dto.response;

import com.aline.core.model.account.AccountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This DTO is the model for account
 * resource payload that is returned by
 * the API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {

    private Long id;
    private AccountType type;
    private String accountNumber;
    private String status;
    private Integer balance;
    private Integer availableBalance;
    private Float apy;

}
