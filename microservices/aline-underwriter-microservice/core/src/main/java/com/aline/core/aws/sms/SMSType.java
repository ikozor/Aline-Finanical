package com.aline.core.aws.sms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SMSType {
    PROMOTIONAL("Promotional"),
    TRANSACTIONAL("Transactional");

    private final String type;
}
