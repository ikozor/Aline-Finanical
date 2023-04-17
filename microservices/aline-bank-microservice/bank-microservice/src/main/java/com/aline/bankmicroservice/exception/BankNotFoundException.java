package com.aline.bankmicroservice.exception;

import com.aline.core.exception.NotFoundException;

public class BankNotFoundException extends NotFoundException {
    public BankNotFoundException() {
        super("Bank was not found.");
    }
}
