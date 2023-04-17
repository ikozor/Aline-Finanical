package com.aline.transactionmicroservice.model;

/**
 * Bank account transaction types
 */
public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER_IN,
    TRANSFER_OUT,
    PURCHASE,
    PAYMENT,
    REFUND,
    VOID
}
