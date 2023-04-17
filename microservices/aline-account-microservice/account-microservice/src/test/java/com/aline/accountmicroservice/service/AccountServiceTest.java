package com.aline.accountmicroservice.service;

import com.aline.core.annotation.test.SpringBootUnitTest;
import com.aline.core.annotation.test.SpringTestProperties;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountStatus;
import com.aline.core.model.account.AccountType;
import com.aline.core.model.account.CheckingAccount;
import com.aline.core.model.account.SavingsAccount;
import com.aline.core.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootUnitTest(SpringTestProperties.DISABLE_WEB_SECURITY)
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @MockBean
    AccountRepository repository;

    @BeforeEach
    void setUp() {
        Account checkingAccount = CheckingAccount.builder()
                .id(1L)
                .accountNumber("123456789")
                .balance(10000)
                .availableBalance(10000)
                .status(AccountStatus.ACTIVE)
                .build();
        Account savingsAccount = SavingsAccount.builder()
                .id(2L)
                .accountNumber("123456780")
                .balance(1000000)
                .status(AccountStatus.ACTIVE)
                .build();
        when(repository.findById(1L))
                .thenReturn(Optional.of(checkingAccount));

        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllByMemberId(1L, pageable))
                .thenReturn(new PageImpl<>(Arrays.asList(checkingAccount, savingsAccount), pageable, 2));
    }

    @Test
    void test_getAccountById_returns_the_correctAccount() {

        Account account = accountService.getAccountById(1L);
        assertNotNull(account);
        assertEquals(1L, account.getId());
        assertEquals("123456789", account.getAccountNumber());
        assertEquals(AccountType.CHECKING, account.getAccountType());
        assertEquals(10000, account.getBalance());
        assertTrue(account instanceof CheckingAccount);

    }

    @Test
    void test_getAccountById_returns_a_page_of_allAccounts() {

        Page<Account> accounts = accountService.getAccountsByMemberId(1L, PageRequest.of(0, 10));
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.getTotalElements());
        assertEquals(1L, accounts.getContent().get(0).getId());
        assertEquals(2L, accounts.getContent().get(1).getId());
    }

}
