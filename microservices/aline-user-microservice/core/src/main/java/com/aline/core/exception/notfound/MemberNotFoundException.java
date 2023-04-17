package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException() {
        super("Member does not exist.");
    }
}
