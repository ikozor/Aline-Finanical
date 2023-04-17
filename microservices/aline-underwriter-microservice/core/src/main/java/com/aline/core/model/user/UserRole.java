package com.aline.core.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    MEMBER(Roles.MEMBER),
    EMPLOYEE(Roles.EMPLOYEE),
    ADMINISTRATOR(Roles.ADMINISTRATOR);

    private final String role;

    public static final class Roles {
        public static final String MEMBER = "member";
        public static final String EMPLOYEE = "employee";
        public static final String ADMINISTRATOR = "administrator";
    }
}
