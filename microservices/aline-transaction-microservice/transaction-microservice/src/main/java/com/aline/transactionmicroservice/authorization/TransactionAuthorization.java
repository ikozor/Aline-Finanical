package com.aline.transactionmicroservice.authorization;

import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.user.MemberUser;
import com.aline.core.model.user.UserRole;
import com.aline.core.security.service.AbstractAuthorizationService;
import com.aline.transactionmicroservice.dto.TransferFundsRequest;
import com.aline.transactionmicroservice.exception.TransactionNotFoundException;
import com.aline.transactionmicroservice.model.Transaction;
import com.aline.transactionmicroservice.repository.TransactionRepository;
import com.aline.transactionmicroservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Authorization predicates that allow
 * for access of a method.
 */
@Component("authService")
@RequiredArgsConstructor
public class TransactionAuthorization extends AbstractAuthorizationService<Transaction> {

    private final AccountService accountService;
    private final TransactionRepository repository;

    /**
     * Can access transaction
     * @param transaction Transaction to access
     * @return  True if the account the transaction is
     *          applied contains a member that is
     *          represented by the current user.
     */
    @Override
    public boolean canAccess(Transaction transaction) {

        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            return transaction.getAccount().getMembers().contains(member);
        }

        return roleIsManagement();
    }

    /**
     * Can access transaction by ID
     * @param id The ID of the transaction
     * @return  True if the transaction represented by the ID
     *          is applied to an account that the member that
     *          is represented by the current user owns.
     */
    public boolean canAccessById(long id) {
        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            Account account = accountService.getAccountById(id);
            Transaction transaction = repository.findById(id).orElseThrow(TransactionNotFoundException::new);
            return transaction.getAccount().equals(account) && member.getAccounts().contains(account);
        }
        return roleIsManagement();
    }

    /**
     * Can access by account ID
     * @param id The ID of the account
     * @return  True if the account ID provided
     *          represents an account that is owned
     *          by the member that is represented
     *          by the current user.
     */
    public boolean canAccessByAccountId(long id) {
        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            Account account = accountService.getAccountById(id);
            return account.getMembers().contains(member);
        }
        return roleIsManagement();
    }

    /**
     * Can access by account number
     * @param accountNumber The account number
     * @return  True if the account number represents an account
     *          that the current user owns.
     */
    public boolean canAccessByAccountNumber(String accountNumber) {
        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            Account account = accountService.getAccountByAccountNumber(accountNumber);
            return account.getMembers().contains(member);
        }
        return roleIsManagement();
    }

    /**
     * Can access by member ID
     * @param memberId The member ID
     * @return  True if the transaction is applied to an account
     *          that belongs to the member with the specified ID.
     */
    public boolean canAccessByMemberId(long memberId) {
        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            return member.getId() == memberId;
        }
        return roleIsManagement();
    }

    /**
     * Can transfer from an account only if the user owns the account
     * the transaction is transferring from.
     * @param request The transferFundsRequest
     * @return True if the transaction can be applied
     */
    public boolean canTransfer(TransferFundsRequest request) {

        if (getRole() == UserRole.MEMBER) {
            MemberUser user = (MemberUser) getUser();
            Member member = user.getMember();
            Account fromAccount = accountService.getAccountByAccountNumber(request.getFromAccountNumber());
            return fromAccount.getMembers().contains(member);
        }

        return roleIsManagement();
    }
}
