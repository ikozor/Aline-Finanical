package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

public class CardNotFoundException extends NotFoundException {
    public CardNotFoundException() {
        super("Card does not exist.");
    }
}
