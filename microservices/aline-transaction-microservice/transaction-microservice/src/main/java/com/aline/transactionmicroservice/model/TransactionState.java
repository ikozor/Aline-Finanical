package com.aline.transactionmicroservice.model;

public enum TransactionState {
    /**
     * Initial state of transaction. This state is still reversible, meaning
     * the transaction can be deleted by the API.
     */
    CREATED,

    /**
     * The transaction is now being processed. This is where the validity of the transaction
     * is checked and will automatically be reversed if the transaction is invalid.
     * The account funds will not be saved in this state, however, they are checked. This state
     * is where more transaction properties are added such as status, increasing or decreasing, etc..
     */
    PROCESSING,

    /**
     * Posted state means the transactions is now considered a permanent record of the account and
     * can no longer be reversed. Account states are committed to the database, and it cannot be
     * undone unless by another transaction such as a refund.
     */
    POSTED
}
