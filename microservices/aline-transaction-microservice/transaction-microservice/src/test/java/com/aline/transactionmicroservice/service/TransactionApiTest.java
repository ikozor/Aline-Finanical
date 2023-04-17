package com.aline.transactionmicroservice.service;

import com.aline.core.annotation.test.SpringBootUnitTest;
import com.aline.core.annotation.test.SpringTestProperties;
import com.aline.core.dto.request.CardRequest;
import com.aline.core.exception.UnprocessableException;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountType;
import com.aline.core.model.account.CheckingAccount;
import com.aline.core.model.account.CreditCardAccount;
import com.aline.core.model.card.Card;
import com.aline.core.model.credit.CreditLine;
import com.aline.core.model.credit.CreditLineType;
import com.aline.core.repository.AccountRepository;
import com.aline.transactionmicroservice.dto.CreateTransaction;
import com.aline.transactionmicroservice.dto.Receipt;
import com.aline.transactionmicroservice.dto.TransferFundsRequest;
import com.aline.transactionmicroservice.model.Merchant;
import com.aline.transactionmicroservice.model.Transaction;
import com.aline.transactionmicroservice.model.TransactionMethod;
import com.aline.transactionmicroservice.model.TransactionState;
import com.aline.transactionmicroservice.model.TransactionStatus;
import com.aline.transactionmicroservice.model.TransactionType;
import com.aline.transactionmicroservice.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootUnitTest(SpringTestProperties.DISABLE_WEB_SECURITY)
@Sql(scripts = {"classpath:scripts/transactions.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
@Slf4j(topic = "Transaction API Test")
class TransactionApiTest {

    @Autowired
    TransactionApi transactions;

    @Autowired
    TransactionRepository repository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardService cardService;

    @Nested
    @DisplayName("Test CREATED state transaction")
    class CreateTransactionsTest {

        @Test
        void test_createsTransactionWithCorrectProperties_and_accountBalanceNotAffected_and_createsNewMerchant() {

            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(10000)
                    .accountNumber("0011011234")
                    .type(TransactionType.PURCHASE)
                    .merchantCode("NEWME")
                    .merchantName("New Merchant")
                    .method(TransactionMethod.ACH)
                    .build();

            Transaction transaction = transactions.createTransaction(createTransaction);
            Account account = transaction.getAccount();
            Merchant merchant = transaction.getMerchant();

            // Account balance not affected
            assertEquals(100000, account.getBalance());

            // Merchant and account information are correct
            assertEquals("0011011234", account.getAccountNumber());
            assertEquals("NEWME", merchant.getCode());
            assertEquals("New Merchant", merchant.getName());

            assertEquals(TransactionType.PURCHASE, transaction.getType());
            assertFalse(transaction.isIncreasing());
            assertTrue(transaction.isDecreasing());
            assertTrue(transaction.isMerchantTransaction());

        }

        @Test
        void test_createsTransactionWithCorrectProperties_for_withdrawal() {

            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(10000)
                    .accountNumber("0011011234")
                    .type(TransactionType.WITHDRAWAL)
                    .method(TransactionMethod.ACH)
                    .build();

            Transaction transaction = transactions.createTransaction(createTransaction);
            Account account = transaction.getAccount();

            // Account balance not affected
            assertEquals(100000, account.getBalance());

            // Merchant and account information are correct
            assertEquals("0011011234", account.getAccountNumber());

            assertEquals(TransactionType.WITHDRAWAL, transaction.getType());
            assertFalse(transaction.isIncreasing());
            assertTrue(transaction.isDecreasing());
            assertFalse(transaction.isMerchantTransaction());

        }

        @Test
        void test_createsTransactionWithCorrectProperties_with_existingMerchant() {

            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(10000)
                    .accountNumber("0011011234")
                    .type(TransactionType.PURCHASE)
                    .merchantCode("ALINE")
                    .merchantName("Aline Financial Online Store")
                    .method(TransactionMethod.ACH)
                    .build();

            Transaction transaction = transactions.createTransaction(createTransaction);
            Account account = transaction.getAccount();
            Merchant merchant = transaction.getMerchant();

            // Account balance not affected
            assertEquals(100000, account.getBalance());

            // Merchant and account information are correct
            assertEquals("0011011234", account.getAccountNumber());
            assertEquals("ALINE", merchant.getCode());
            assertNotEquals(createTransaction.getMerchantName(), merchant.getName()); // If Merchant exists use existing name
            assertEquals("Aline Financial", merchant.getName());

            assertEquals(TransactionType.PURCHASE, transaction.getType());
            assertFalse(transaction.isIncreasing());
            assertTrue(transaction.isDecreasing());
            assertTrue(transaction.isMerchantTransaction());

        }

        @Test
        void test_canDelete() {
            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(10000)
                    .accountNumber("0011011234")
                    .type(TransactionType.PURCHASE)
                    .merchantCode("ALINE")
                    .merchantName("Aline Financial Online Store")
                    .method(TransactionMethod.ACH)
                    .build();

            Transaction transaction = transactions.createTransaction(createTransaction);
            Account account = transaction.getAccount();
            Merchant merchant = transaction.getMerchant();

            // Account balance not affected
            assertEquals(100000, account.getBalance());

            // Merchant and account information are correct
            assertEquals("0011011234", account.getAccountNumber());
            assertEquals("ALINE", merchant.getCode());
            assertNotEquals(createTransaction.getMerchantName(), merchant.getName()); // If Merchant exists use existing name
            assertEquals("Aline Financial", merchant.getName());

            assertEquals(TransactionType.PURCHASE, transaction.getType());
            assertFalse(transaction.isIncreasing());
            assertTrue(transaction.isDecreasing());
            assertTrue(transaction.isMerchantTransaction());

            assertDoesNotThrow(() -> transactions.deleteTransactionById(transaction.getId()));

            assertNull(repository.findById(transaction.getId()).orElse(null));
        }

        @Test
        void test_createsTransactionWithCorrectProperties_and_customDate_dateIsCorrect() {

            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(10000)
                    .accountNumber("0011011234")
                    .type(TransactionType.PURCHASE)
                    .merchantCode("NEWME")
                    .merchantName("New Merchant")
                    .method(TransactionMethod.ACH)
                    .date(LocalDate.of(2022, 1, 1).atStartOfDay())
                    .build();

            Transaction transaction = transactions.createTransaction(createTransaction);
            Account account = transaction.getAccount();
            Merchant merchant = transaction.getMerchant();

            // Account balance not affected
            assertEquals(100000, account.getBalance());

            // Merchant and account information are correct
            assertEquals("0011011234", account.getAccountNumber());
            assertEquals("NEWME", merchant.getCode());
            assertEquals("New Merchant", merchant.getName());

            assertEquals(TransactionType.PURCHASE, transaction.getType());
            assertFalse(transaction.isIncreasing());
            assertTrue(transaction.isDecreasing());
            assertTrue(transaction.isMerchantTransaction());
            assertEquals(LocalDate.of(2022, 1, 1).atStartOfDay(), transaction.getDate());

        }
    }

    @Nested
    @DisplayName("Test PROCESSING state transaction")
    class ProcessingTransactionsTest {

        @Test
        void test_unprocessableWhenTransactionPosted() {
            Transaction transaction = new Transaction();
            transaction.setState(TransactionState.POSTED);
            assertThrows(UnprocessableException.class, () -> transactions.processTransaction(transaction));
        }

        @Test
        void test_receiptContainsCorrectInformation_purchaseChecking() {
            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(10000)
                    .accountNumber("0011011234")
                    .type(TransactionType.PURCHASE)
                    .merchantCode("ALINE")
                    .merchantName("Aline Financial Online Store")
                    .method(TransactionMethod.ACH)
                    .build();

            Transaction transaction = transactions.createTransaction(createTransaction);

            assertEquals(TransactionState.CREATED, transaction.getState());

            Receipt receipt = transactions.processTransaction(transaction);
            CheckingAccount account = (CheckingAccount) transaction.getAccount();

            assertEquals(transaction.getId(), receipt.getId());
            assertEquals(transaction.getMethod(), receipt.getMethod());
            assertEquals(transaction.getType(), receipt.getType());
            assertEquals(TransactionStatus.APPROVED, receipt.getStatus());
            assertEquals(90000, account.getAvailableBalance());
            assertEquals(createTransaction.getAmount(), transaction.getAmount());

        }

        @Test
        void test_receiptContainsCorrectInformation_depositChecking() {
            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(100000)
                    .accountNumber("0011011234")
                    .merchantCode("ALINE")
                    .type(TransactionType.DEPOSIT)
                    .method(TransactionMethod.ACH)
                    .build();

            Transaction transaction = transactions.createTransaction(createTransaction);

            assertEquals(TransactionState.CREATED, transaction.getState());

            Receipt receipt = transactions.processTransaction(transaction);
            CheckingAccount account = (CheckingAccount) transaction.getAccount();

            assertEquals(transaction.getId(), receipt.getId());
            assertEquals(transaction.getMethod(), receipt.getMethod());
            assertEquals(transaction.getType(), receipt.getType());
            assertEquals(TransactionStatus.APPROVED, receipt.getStatus());
            assertEquals(200000, account.getBalance());
            assertEquals(200000, account.getAvailableBalance());
            assertEquals(createTransaction.getAmount(), transaction.getAmount());
        }

    }

    @Nested
    @DisplayName("Test POST state transactions")
    class PostTransactionsTest {

        @Test
        void test_bankAccountBalanceDecreaseCorrectAmount_when_transactionApproved() {

            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(5000) // $50.00
                    .merchantCode("ALINE")
                    .accountNumber("0011011234")
                    .type(TransactionType.PURCHASE)
                    .method(TransactionMethod.ACH)
                    .build();

            Account preTransactionAccount = accountRepository.findByAccountNumber(createTransaction.getAccountNumber())
                    .orElse(null);

            assertNotNull(preTransactionAccount);
            assertEquals(AccountType.CHECKING, preTransactionAccount.getAccountType());

            int initialBalance = preTransactionAccount.getBalance();

            Transaction transaction = transactions.createTransaction(createTransaction);

            Receipt receipt = transactions.processTransaction(transaction);
            log.info("Receipt: {}", receipt);

            Account account = accountRepository.findByAccountNumber(createTransaction.getAccountNumber())
                    .orElse(null);

            assertNotNull(account);
            assertEquals(AccountType.CHECKING, account.getAccountType());
            CheckingAccount checkingAccount = (CheckingAccount) account;

            assertEquals(95000, checkingAccount.getBalance());
            assertEquals(95000, checkingAccount.getAvailableBalance());
            assertEquals(initialBalance, transaction.getInitialBalance());
            assertEquals(95000, transaction.getPostedBalance());
            assertEquals(createTransaction.getAmount(), transaction.getAmount());

        }

        @Test
        void test_bankAccountBalanceDecreaseCorrectAmount_when_transactionApproved_debitCard() {

            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(5000) // $50.00
                    .merchantCode("ALINE")
                    .cardRequest(CardRequest.builder()
                            .cardNumber("4490246198724138")
                            .securityCode("123")
                            .expirationDate(LocalDate.of(2025, 8, 1))
                            .build())
                    .type(TransactionType.PURCHASE)
                    .method(TransactionMethod.DEBIT_CARD)
                    .build();

            Card card = cardService.getCardByCardRequest(createTransaction.getCardRequest());

            Account preTransactionAccount = card.getAccount();

            assertNotNull(preTransactionAccount);
            assertEquals(AccountType.CHECKING, preTransactionAccount.getAccountType());

            int initialBalance = preTransactionAccount.getBalance();

            Transaction transaction = transactions.createTransaction(createTransaction);

            Receipt receipt = transactions.processTransaction(transaction);
            log.info("Receipt: {}", receipt);

            Account account = card.getAccount();

            assertNotNull(account);
            assertEquals(AccountType.CHECKING, account.getAccountType());
            CheckingAccount checkingAccount = (CheckingAccount) account;

            assertEquals(95000, checkingAccount.getBalance());
            assertEquals(95000, checkingAccount.getAvailableBalance());
            assertEquals(initialBalance, transaction.getInitialBalance());
            assertEquals(95000, transaction.getPostedBalance());
            assertEquals(createTransaction.getAmount(), transaction.getAmount());

        }

        @Test
        void test_bankAccountBalanceIncrease_when_transactionApproved() {
            CreateTransaction paycheck = CreateTransaction.builder()
                    .amount(500000)
                    .method(TransactionMethod.ACH)
                    .type(TransactionType.DEPOSIT)
                    .merchantCode("ALINE")
                    .accountNumber("0011011234")
                    .build();

            Account account = accountRepository.findByAccountNumber(paycheck.getAccountNumber())
                    .orElse(null);

            assertNotNull(account);

            int initialBalance = account.getBalance();

            Transaction transaction = transactions.createTransaction(paycheck);

            Receipt receipt = transactions.processTransaction(transaction);
            log.info("Receipt: {}", receipt);

            CheckingAccount checkingAccount = (CheckingAccount) accountRepository
                    .findByAccountNumber(paycheck
                            .getAccountNumber()).orElse(null);

            assertNotNull(checkingAccount);
            assertEquals(600000, checkingAccount.getBalance());
            assertEquals(600000, checkingAccount.getAvailableBalance());
            assertEquals(initialBalance, transaction.getInitialBalance());
            assertEquals(600000, transaction.getPostedBalance());
            assertEquals(paycheck.getAmount(), transaction.getAmount());

        }

        @Test
        void test_bankAccountBalanceCorrectAmount_when_transactionDenied() {

            CreateTransaction createTransaction = CreateTransaction.builder()
                    .amount(5000000)
                    .merchantCode("ALINE")
                    .accountNumber("0011011234")
                    .type(TransactionType.PURCHASE)
                    .method(TransactionMethod.ACH)
                    .build();

            Account preTransactionAccount = accountRepository.findByAccountNumber(createTransaction.getAccountNumber())
                    .orElse(null);

            assertNotNull(preTransactionAccount);
            assertEquals(AccountType.CHECKING, preTransactionAccount.getAccountType());

            int initialBalance = preTransactionAccount.getBalance();

            Transaction transaction = transactions.createTransaction(createTransaction);
            assertEquals(TransactionStatus.PENDING, transaction.getStatus());

            Receipt receipt = transactions.processTransaction(transaction);
            log.info("Receipt: {}", receipt);
            assertEquals(TransactionStatus.DENIED, receipt.getStatus());

            Account account = accountRepository.findByAccountNumber(createTransaction.getAccountNumber())
                    .orElse(null);

            assertNotNull(account);
            assertEquals(AccountType.CHECKING, account.getAccountType());
            CheckingAccount checkingAccount = (CheckingAccount) account;

            assertEquals(initialBalance, checkingAccount.getBalance());
            assertEquals(initialBalance, checkingAccount.getAvailableBalance());
            assertEquals(initialBalance, transaction.getInitialBalance());
            assertEquals(createTransaction.getAmount(), transaction.getAmount());
            assertEquals(initialBalance, transaction.getPostedBalance());

        }

    }

    @Nested
    @DisplayName("Credit Card Transaction Test")
    class CreditCardTransactionTest {

        @Test
        void test_creditCard_purchaseTransaction_approved() {
            CardRequest cardRequest = CardRequest.builder()
                    .cardNumber("4929322248222398")
                    .expirationDate(LocalDate.of(2025, 8, 1))
                    .securityCode("123")
                    .build();
            CreateTransaction createTransaction = CreateTransaction.builder()
                    .type(TransactionType.PURCHASE)
                    .method(TransactionMethod.CREDIT_CARD)
                    .cardRequest(cardRequest)
                    .merchantCode("ALINE")
                    .amount(50000)
                    .build();

            Card card = cardService.getCardByCardRequest(cardRequest);
            assertNotNull(card);

            Account account = card.getAccount();
            assertNotNull(account);

            assertTrue(account instanceof CreditCardAccount);

            CreditCardAccount creditCardAccount = (CreditCardAccount) account;

            CreditLine creditLine = creditCardAccount.getCreditLine();

            assertNotNull(creditLine);
            assertEquals(500000, creditLine.getCreditLimit());
            assertEquals(CreditLineType.STANDARD, creditLine.getCreditLineType());

            assertEquals(0, account.getBalance());

            Transaction transaction = transactions.createTransaction(createTransaction);

            Receipt receipt = transactions.processTransaction(transaction);
            log.info("Receipt: {}", receipt);
            assertEquals(TransactionStatus.APPROVED, receipt.getStatus());

            CreditCardAccount postTransactionAccount = (CreditCardAccount) accountRepository.findByAccountNumber(account.getAccountNumber()).orElse(null);
            assertNotNull(postTransactionAccount);

            assertEquals(postTransactionAccount, transaction.getAccount());
            assertEquals(50000, postTransactionAccount.getBalance());
            assertEquals(450000, postTransactionAccount.getAvailableCredit());

        }

        @Test
        void test_creditCard_purchaseTransaction_denied() {
            CardRequest cardRequest = CardRequest.builder()
                    .cardNumber("4929322248222398")
                    .expirationDate(LocalDate.of(2025, 8, 1))
                    .securityCode("123")
                    .build();
            CreateTransaction createTransaction = CreateTransaction.builder()
                    .type(TransactionType.PURCHASE)
                    .method(TransactionMethod.CREDIT_CARD)
                    .cardRequest(cardRequest)
                    .merchantCode("ALINE")
                    .amount(1000000) // Way over credit limit
                    .build();

            Card card = cardService.getCardByCardRequest(cardRequest);
            assertNotNull(card);

            Account account = card.getAccount();
            assertNotNull(account);

            assertTrue(account instanceof CreditCardAccount);

            CreditCardAccount creditCardAccount = (CreditCardAccount) account;

            CreditLine creditLine = creditCardAccount.getCreditLine();

            assertNotNull(creditLine);
            assertEquals(500000, creditLine.getCreditLimit());
            assertEquals(CreditLineType.STANDARD, creditLine.getCreditLineType());

            assertEquals(0, account.getBalance());

            Transaction transaction = transactions.createTransaction(createTransaction);

            Receipt receipt = transactions.processTransaction(transaction);
            log.info("Receipt: {}", receipt);
            assertEquals(TransactionStatus.DENIED, receipt.getStatus());

            CreditCardAccount postTransactionAccount = (CreditCardAccount) accountRepository.findByAccountNumber(account.getAccountNumber()).orElse(null);
            assertNotNull(postTransactionAccount);

        }

    }

    @Nested
    @DisplayName("Transfer Funds Test")
    class TransferFundsTests {

        @Test
        void test_transferFunds_accountsReflectNewBalance() {
            TransferFundsRequest request = TransferFundsRequest.builder()
                    .fromAccountNumber("0011011234")
                    .toAccountNumber("0012021234")
                    .amount(10000)
                    .build();

            Receipt[] receipts = transactions.transferFunds(request);
            assertEquals(2, receipts.length);

            Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                    .orElse(null);

            Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                    .orElse(null);

            assertNotNull(fromAccount);
            assertNotNull(toAccount);

            assertEquals(90000, fromAccount.getBalance());
            assertEquals(10010000, toAccount.getBalance());

        }

        @Test
        void test_transferFunds_denyOutTransaction_when_notEnoughFunds_fromAccount() {
            TransferFundsRequest request = TransferFundsRequest.builder()
                    .fromAccountNumber("0011011234")
                    .toAccountNumber("0012021234")
                    .amount(500000)
                    .build();

            Receipt[] receipts = transactions.transferFunds(request);
            assertEquals(2, receipts.length);
            assertEquals(receipts[0].getStatus(), TransactionStatus.DENIED);
            assertEquals(receipts[1].getStatus(), TransactionStatus.DENIED);

            Transaction outTransaction = repository.findById(receipts[1].getId())
                    .orElse(null);

            Transaction inTransaction = repository.findById(receipts[0].getId())
                    .orElse(null);

            assertNotNull(outTransaction);
            assertNotNull(inTransaction);

            assertEquals(outTransaction.getStatus(), TransactionStatus.DENIED);
            assertEquals(inTransaction.getStatus(), TransactionStatus.DENIED);

            Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                    .orElse(null);

            Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                    .orElse(null);

            assertNotNull(fromAccount);
            assertNotNull(toAccount);

            assertEquals(100000, fromAccount.getBalance());
            assertEquals(10000000, toAccount.getBalance());

        }

    }

}
