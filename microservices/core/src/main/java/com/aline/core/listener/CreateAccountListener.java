package com.aline.core.listener;

import com.aline.core.model.Branch;
import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountType;
import com.aline.core.util.RandomNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;

/**
 * The CreateAccountLister class introduces
 * side effects to the {@link Account} entity
 * using the JPA Entity life-cycle annotations.
 */
@Component
@Slf4j(topic = "Create Account Listener")
public class CreateAccountListener {

    private RandomNumberGenerator generator;

    @Autowired
    public void setGenerator(RandomNumberGenerator generator) {
        this.generator = generator;
    }

    @PrePersist
    public void prePersist(Account account) {
        Member primaryMember = account.getPrimaryAccountHolder();
        Branch primaryBranch = primaryMember.getBranch();
        long branchId = primaryBranch.getId();
        AccountType accountType = account.getAccountType();

        // Branch Segment
        // Branch ID: 22
        // Segment: 022
        String branchSegment = StringUtils.leftPad(String.valueOf(branchId), 3, "0");

        // Account Type Segment
        // Account Type Ordinal: 1
        // Segment: 101
        String accountTypeSegment = String.format("%d0%d", accountType.ordinal() + 1, accountType.ordinal() + 1);

        // RNG Segment
        String rngSegment = generator.generateRandomNumberString(4);

        String accountNumber = String.format("%s%s%s", branchSegment, accountTypeSegment, rngSegment);
        account.setAccountNumber(accountNumber);
    }

}
