package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

public class CardIssuerNotFound extends NotFoundException {
    public CardIssuerNotFound() {
        super("Card issuer does not exist.");
    }
}
