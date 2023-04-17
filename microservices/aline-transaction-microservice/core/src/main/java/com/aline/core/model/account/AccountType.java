package com.aline.core.model.account;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AccountType {
    CHECKING(Values.CHECKING),
    SAVINGS(Values.SAVINGS),
    CREDIT_CARD(Values.CREDIT_CARD),
    LOAN(Values.LOAN);

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    public static final class Values {
        public static final String CHECKING = "CHECKING";
        public static final String SAVINGS = "SAVINGS";
        public static final String CREDIT_CARD = "CREDIT_CARD";
        public static final String LOAN = "LOAN";
    }
}
