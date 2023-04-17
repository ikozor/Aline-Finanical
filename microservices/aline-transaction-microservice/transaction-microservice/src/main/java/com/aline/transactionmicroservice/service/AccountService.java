package com.aline.transactionmicroservice.service;

import com.aline.core.exception.notfound.AccountNotFoundException;
import com.aline.core.model.account.Account;
import com.aline.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    /**
     * Get an account by the account number
     * @param accountNumber The account number string
     * @return An account associated with the specified account number
     * @throws AccountNotFoundException when the account does not exist
     */
    public Account getAccountByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
    }

    /**
     * Get an account by the ID
     * @param id The ID of the account
     * @return An account associated with the specified ID
     * @throws AccountNotFoundException when the accound does not exist
     */
    public Account getAccountById(long id) {
        return repository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    /**
     * Get a masked account number (Only the last four will show)
     * @param accountNumber The raw account number
     * @return A masked account number string
     */
    public String maskAccountNumber(String accountNumber) {
        return StringUtils.leftPad(accountNumber
                        .substring(accountNumber.length() - 4),
                        10, "*");
    }

    /**
     * Save an account entity
     * @param account The account to save
     */
    public void saveAccount(Account account) {
        repository.save(account);
    }

}
