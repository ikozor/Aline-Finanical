package com.aline.transactionmicroservice.exception;

import com.aline.core.exception.BadRequestException;

/**
 * Exception for an attempt to delete a posted transaction is made
 */
public class TransactionPostedException extends BadRequestException {
    public TransactionPostedException() {
        super("Transaction has been posted. It cannot be deleted.");
    }
}
