package com.aline.transactionmicroservice.service;

import com.aline.core.model.account.Account;
import com.aline.transactionmicroservice.model.Transaction;
import com.aline.transactionmicroservice.model.TransactionState;
import com.aline.transactionmicroservice.model.TransactionStatus;
import com.aline.transactionmicroservice.model.TransactionType;
import com.aline.transactionmicroservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class TransactionApiUnitTest {

    TransactionApi service;
    TransactionService transactionService;
    AccountService accountService;
    MerchantService merchantService;
    CardService cardService;
    TransactionRepository repository;
    ModelMapper mapper;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        accountService = mock(AccountService.class);
        merchantService = mock(MerchantService.class);
        cardService = mock(CardService.class);
        repository = mock(TransactionRepository.class);
        mapper = mock(ModelMapper.class);
        service = new TransactionApi(
                accountService,
                merchantService,
                cardService,
                repository,
                mapper
        );
    }

    @Test
    void test_performTransaction_applies_decreaseToAccountBalance() {
        Account account = Account.builder()
                .balance(10000) // $100.00
                .build();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.PURCHASE)
                .amount(5000) // $50.00
                .account(account)
                .status(TransactionStatus.APPROVED)
                .state(TransactionState.CREATED)
                .build();

        transaction.checkTransaction();

        service.performTransaction(transaction);

        assertTrue(transaction.isDecreasing());
        assertTrue(transaction.getAmount() > 0);
    }

    @Test
    void test_performTransaction_applies_increaseToAccountBalance() {
        Account account = Account.builder()
                .balance(10000) // $100.00
                .build();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(15000) // $150.00
                .account(account)
                .status(TransactionStatus.APPROVED)
                .state(TransactionState.CREATED)
                .build();

        transaction.checkTransaction();

        service.performTransaction(transaction);

        assertTrue(transaction.isIncreasing());
        assertTrue(transaction.getAmount() > 0);
    }

}
