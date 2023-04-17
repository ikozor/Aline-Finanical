package com.aline.core.exception.badgateway;

import com.aline.core.exception.BadGatewayException;

public class EmailNotSentException extends BadGatewayException {
    public EmailNotSentException() {
        super("Email failed to send.");
    }
}
