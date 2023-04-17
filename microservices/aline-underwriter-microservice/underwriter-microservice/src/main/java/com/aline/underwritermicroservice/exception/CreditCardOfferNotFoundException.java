package com.aline.underwritermicroservice.exception;

import com.aline.core.exception.NotFoundException;

public class CreditCardOfferNotFoundException extends NotFoundException {
    public CreditCardOfferNotFoundException() {
        super("Credit card offer does not exist.");
    }
}
