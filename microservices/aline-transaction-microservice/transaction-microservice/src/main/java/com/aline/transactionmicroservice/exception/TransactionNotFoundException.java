package com.aline.transactionmicroservice.exception;

import com.aline.core.exception.NotFoundException;

public class TransactionNotFoundException extends NotFoundException {
    public TransactionNotFoundException() {
        super("Transaction does not exist.");
    }
}
