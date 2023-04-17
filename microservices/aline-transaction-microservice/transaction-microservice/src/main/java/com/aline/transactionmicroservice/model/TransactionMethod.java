package com.aline.transactionmicroservice.model;

import lombok.RequiredArgsConstructor;

/**
 * Transaction methods
 */
@RequiredArgsConstructor
public enum TransactionMethod {
    ACH,
    ATM,
    CREDIT_CARD,
    DEBIT_CARD,
    APP
}
