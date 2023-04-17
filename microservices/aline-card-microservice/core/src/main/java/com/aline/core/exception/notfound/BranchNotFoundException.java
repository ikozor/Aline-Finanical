package com.aline.core.exception.notfound;

import com.aline.core.exception.NotFoundException;

public class BranchNotFoundException extends NotFoundException {
    public BranchNotFoundException() {
        super("Branch does not exist.");
    }
}
