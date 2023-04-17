package com.aline.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ContactMethod {
    @JsonProperty("phone")
    PHONE,
    @JsonProperty("email")
    EMAIL
}
