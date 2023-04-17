package com.aline.transactionmicroservice.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCriteria {
    private String[] searchTerms;
    private TransactionCriteriaMode mode;
    private long accountId;
    private long memberId;
}
