package com.aline.core.validation.validators;

import com.aline.core.validation.annotation.AccountNumber;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountNumberValidator implements ConstraintValidator<AccountNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        boolean matchRegexPattern = value.matches("[0-9]{8,10}");
        boolean accountTypeSegmentIsPalindrome = false;
        if (matchRegexPattern) {
            String accountTypeSegment = value.substring(value.length() - 7, value.length() - 4);
            accountTypeSegmentIsPalindrome = StringUtils.reverse(accountTypeSegment).equals(accountTypeSegment);
        }
        return matchRegexPattern && accountTypeSegmentIsPalindrome;
    }
}
