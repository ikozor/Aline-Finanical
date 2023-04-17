package com.aline.transactionmicroservice.dto;

import com.aline.transactionmicroservice.model.TransactionMethod;
import com.aline.transactionmicroservice.model.TransactionStatus;
import com.aline.transactionmicroservice.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Receipt {
    private long id;
    private TransactionType type;
    private TransactionMethod method;
    private TransactionStatus status;
    private int amount;
    private String accountNumber;
    private String cardNumber;
    private String description;
    private MerchantResponse merchantResponse;
}
