package com.aline.transactionmicroservice.dto;

import com.aline.transactionmicroservice.model.TransactionMethod;
import com.aline.transactionmicroservice.model.TransactionStatus;
import com.aline.transactionmicroservice.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard DTO to represent a transaction
 * <br>
 * <em>This is what should be returned when retrieving
 * transaction information through the API.</em>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private long id;
    private TransactionMethod method;
    private int amount;
    private String accountNumber;
    private int initialBalance;
    private int postedBalance;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private MerchantResponse merchant;
    private LocalDateTime date;
}
