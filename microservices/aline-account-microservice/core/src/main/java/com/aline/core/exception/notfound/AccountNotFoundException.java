package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

/**
 * Bank account does not exist.
 */
public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException() {
        super("Account does not exist.");
    }
}
