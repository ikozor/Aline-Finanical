package com.aline.transactionmicroservice.exception;

import com.aline.core.exception.NotFoundException;

public class MerchantNotFoundException extends NotFoundException {
    public MerchantNotFoundException() {
        super("Merchant does not exist.");
    }
}
