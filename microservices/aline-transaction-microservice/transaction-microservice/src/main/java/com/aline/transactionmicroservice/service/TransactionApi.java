package com.aline.transactionmicroservice.service;

import com.aline.core.dto.request.CardRequest;
import com.aline.core.exception.BadRequestException;
import com.aline.core.exception.UnprocessableException;
import com.aline.core.exception.notfound.AccountNotFoundException;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountType;
import com.aline.core.model.account.CheckingAccount;
import com.aline.core.model.account.CreditCardAccount;
import com.aline.core.model.card.Card;
import com.aline.core.security.annotation.RoleIsManagement;
import com.aline.transactionmicroservice.dto.CreateTransaction;
import com.aline.transactionmicroservice.dto.MerchantResponse;
import com.aline.transactionmicroservice.dto.Receipt;
import com.aline.transactionmicroservice.dto.TransferFundsRequest;
import com.aline.transactionmicroservice.exception.TransactionNotFoundException;
import com.aline.transactionmicroservice.exception.TransactionPostedException;
import com.aline.transactionmicroservice.model.Merchant;
import com.aline.transactionmicroservice.model.Transaction;
import com.aline.transactionmicroservice.model.TransactionMethod;
import com.aline.transactionmicroservice.model.TransactionState;
import com.aline.transactionmicroservice.model.TransactionStatus;
import com.aline.transactionmicroservice.model.TransactionType;
import com.aline.transactionmicroservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Post transaction service handles the processing
 * and posting of transactions created by users
 * using the public Transaction API. The transaction API can
 * only be accessed by authorized users whether that be
 * members, vendors, or other authorized individuals. They
 * will be provided an API key. They will register as a vendor
 * with the bank first in order to accept payment from our cards
 * and checks. Afterwards, their systems should be able to access
 * our services to create transactions and receive funds.
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "Transactions")
public class TransactionApi {
    private final AccountService accountService;
    private final MerchantService merchantService;
    private final CardService cardService;
    private final TransactionRepository repository;
    private final ModelMapper mapper;

    @Transactional(rollbackOn = {
            AccountNotFoundException.class
    })
    public Transaction createTransaction(@Valid CreateTransaction createTransaction) {
        log.info("Creating transaction...");
        log.debug("Create transaction DTO: {}", createTransaction);
        Transaction transaction = mapper.map(createTransaction, Transaction.class);
        transaction.setMethod(createTransaction.getMethod());

        CardRequest cardRequest = createTransaction.getCardRequest();
        Account account;
        if (cardRequest != null) {
            Card card = cardService.getCardByCardRequest(createTransaction.getCardRequest());

            // Check if card is expired
            if (LocalDate.now().isAfter(card.getExpirationDate()))
                throw new BadRequestException("Card is expired.");

            account = card.getAccount();
            String currentDescription = transaction.getDescription();
            String cardNumber = card.getCardNumber().substring(card.getCardNumber().length() - 4);
            String cardDescription = String.format("%s using card ending in %s - %s", transaction.getType().name(), cardNumber, currentDescription);
            transaction.setDescription(cardDescription);
        } else {
            account = accountService.getAccountByAccountNumber(createTransaction.getAccountNumber());
        }

        if (account == null)
            throw new BadRequestException("No account found for this transaction.");

        transaction.setAccount(account);
        transaction.setInitialBalance(account.getBalance());

        if (isMerchantTransaction(createTransaction.getType())) {
            Merchant merchant = merchantService.checkMerchant(
                    createTransaction.getMerchantCode(),
                    createTransaction.getMerchantName());
            transaction.setMerchant(merchant);
        } else {
            transaction.setMerchant(merchantService.getMerchantByCode("NONE"));
        }

        transaction.setStatus(TransactionStatus.PENDING); // Transactions will initially be pending when created
        transaction.setState(TransactionState.CREATED);
        if (transaction.getDate() == null)
            transaction.setDate(LocalDateTime.now());
        log.info("Transaction created and set to PENDING at {}", transaction.getDate());
        return repository.save(transaction);
    }

    /**
     * Boolean representing whether the transaction type
     * can be performed by a merchant.
     * @param type TransactionType enum
     * @return True if the transaction type can be performed by a merchant
     */
    public boolean isMerchantTransaction(TransactionType type) {
        switch (type) {
            case PURCHASE:
            case PAYMENT:
            case REFUND:
            case VOID:
            case DEPOSIT:
                return true;
            default:
                return false;
        }
    }

    /**
     * Processes an initialized transaction
     * @param transaction   An initialized transaction.
     *                      In order to initialize a transaction,
     *                      please see {@link #createTransaction(CreateTransaction)}.
     * @return A receipt of the processed transaction.
     */
    @Transactional(rollbackOn = {
            UnprocessableException.class
    })
    public Receipt processTransaction(Transaction transaction) {

        if (transaction.getState() == TransactionState.POSTED)
            throw new UnprocessableException("Transaction is already posted. Unable to process a transaction.");

        transaction.setState(TransactionState.PROCESSING);

        // Perform the passed transaction
        performTransaction(transaction);
        validateTransaction(transaction);
        postTransaction(transaction);

        return mapToReceipt(transaction);
    }

    public Receipt mapToReceipt(Transaction transaction) {
        val receipt = mapper.map(transaction, Receipt.class);

        if (transaction.isMerchantTransaction()) {
            receipt.setMerchantResponse(mapper.map(
                    transaction.getMerchant(),
                    MerchantResponse.class));
        }

        return receipt;
    }

    /**
     * Approve the transaction
     * @param transaction The transaction to approve
     */
    public void approveTransaction(Transaction transaction) {
        log.info("Approving transaction {}...", transaction.getId());
        transaction.setStatus(TransactionStatus.APPROVED);
        performTransaction(transaction);
    }

    /**
     * Deny the transaction
     * @param transaction The transaction to deny
     */
    public void denyTransaction(Transaction transaction) {
        log.info("Denying transaction {}...", transaction.getId());
        transaction.setStatus(TransactionStatus.DENIED);
        performTransaction(transaction);
    }

    /**
     * Apply transaction increase or decrease balance to the
     * account attached to the transaction. The transaction will
     * only be performed if it has been approved.
     * <br>
     * If status is pending and the account is a checking account
     * then the available balance will be updated.
     * @param transaction The transaction to perform
     */
    public void performTransaction(Transaction transaction) {
        boolean isIncreasing = transaction.isIncreasing();
        boolean isDecreasing = transaction.isDecreasing();
        int amount = transaction.getAmount();

        log.info("Performing transaction: [amount={}, isIncreasing={}, isDecreasing={}]",
                amount,
                isIncreasing,
                isDecreasing);

        Account account = transaction.getAccount();

        int postedBalance = account.getBalance();
        log.info("Account {} balance before transaction: {}", account.getId(), postedBalance);

        log.info("Transaction {} is {}", transaction.getId(), transaction.getStatus().toString());

        // If transaction is approved, decrease actual balance
        if (transaction.getStatus() == TransactionStatus.APPROVED) {
            if (isIncreasing && !isDecreasing) {
                postedBalance = account.getBalance() + amount;
            } else if (isDecreasing && !isIncreasing) {
                postedBalance = account.getBalance() - amount;
            }
        } else if (transaction.getStatus() == TransactionStatus.PENDING) {
            // If transaction is pending and account is checking, decrease available balance
            if (account.getAccountType() == AccountType.CHECKING) {
                val checkingAccount = (CheckingAccount) account;
                if (isIncreasing && !isDecreasing) {
                    postedBalance = checkingAccount.getAvailableBalance() + amount;
                } else if (isDecreasing && !isIncreasing) {
                    postedBalance = checkingAccount.getAvailableBalance() - amount;
                }
            } else if (account.getAccountType() == AccountType.SAVINGS) {
                if (isIncreasing && !isDecreasing) {
                    postedBalance = account.getBalance() + amount;
                } else if (isDecreasing && !isIncreasing) {
                    postedBalance = account.getBalance() - amount;
                }
            } else if (account.getAccountType() == AccountType.CREDIT_CARD) {
                if (isIncreasing && !isDecreasing) {
                    postedBalance = account.getBalance() - amount;
                } else if (isDecreasing && !isIncreasing) {
                    postedBalance = account.getBalance() + amount;
                }
            }
            transaction.setPostedBalance(postedBalance);
            return;
        }
        transaction.setPostedBalance(postedBalance);
        log.info("Account {} balance after transactions: {}", account.getId(), postedBalance);
    }

    /**
     * Validate transaction based on account balance
     * @param transaction The transaction to validate
     */
    public void validateTransaction(Transaction transaction) {
        log.info("Validating transaction...");
        if (transaction.getState() != TransactionState.PROCESSING)
            throw new UnprocessableException("Transaction is in an invalid state.");
        if (transaction.getStatus() != TransactionStatus.PENDING)
            throw new UnprocessableException("Transaction already validated.");

        int balance = transaction.getPostedBalance();

        log.info("New posted balance: {}", balance);

        if (transaction.getAccount().getAccountType() == AccountType.CREDIT_CARD) {
            CreditCardAccount account = (CreditCardAccount) transaction.getAccount();

            if (balance > account.getAvailableCredit() && transaction.isDecreasing()) {
                denyTransaction(transaction);
            }
        }

        if (balance < 0 && transaction.isDecreasing()) {
            denyTransaction(transaction);
        }

        // If the status is still pending after all checks
        if (transaction.getStatus() == TransactionStatus.PENDING)
            approveTransaction(transaction);
    }

    /**
     * Set the state of the transaction to POSTED and commit all changes to the database
     * @param transaction The transaction to post
     */
    public void postTransaction(Transaction transaction) {
        log.info("Posting transaction {}", transaction.getId());
        if (transaction.getState() == TransactionState.POSTED)
            throw new UnprocessableException("Transaction is already posted.");
        if (transaction.getState() != TransactionState.PROCESSING)
            throw new UnprocessableException("Transaction needs to be processed before it is posted.");
        if (transaction.getStatus() == TransactionStatus.PENDING)
            throw new UnprocessableException("Cannot post a transaction that is pending.");
        transaction.setState(TransactionState.POSTED);
        if (transaction.getStatus() == TransactionStatus.APPROVED) {
            Account account = transaction.getAccount();
            if (transaction.isIncreasing() && !transaction.isDecreasing()) {
                account.increaseBalance(transaction.getAmount());
                if (account.getAccountType() == AccountType.CHECKING)
                    ((CheckingAccount) account).increaseAvailableBalance(transaction.getAmount());
                if (account.getAccountType() == AccountType.CREDIT_CARD)
                    ((CreditCardAccount) account).increaseAvailableCredit(transaction.getAmount());
            } else if (transaction.isDecreasing() && !transaction.isIncreasing()) {
                account.decreaseBalance(transaction.getAmount());
                if (account.getAccountType() == AccountType.CHECKING)
                    ((CheckingAccount) account).decreaseAvailableBalance(transaction.getAmount());
                if (account.getAccountType() == AccountType.CREDIT_CARD)
                    ((CreditCardAccount) account).decreaseAvailableCredit(transaction.getAmount());
            }
        }
        log.info("Transaction {} {}", transaction.getId(), transaction.getStatus());
        repository.save(transaction);
    }


    /**
     * Delete transaction by its ID
     * @param id The ID of the transaction to delete
     */
    @RoleIsManagement
    public void deleteTransactionById(long id) {
        Transaction transaction = repository.findById(id).orElseThrow(TransactionNotFoundException::new);
        if (transaction.getState() != TransactionState.POSTED) {
            repository.delete(transaction);
        } else throw new TransactionPostedException();
    }

    /**
     * Transfer funds from one account to another using a transfer funds
     * request.
     * @param request The transfer funds request
     * @return An array of 2 receipts
     */
    @PreAuthorize("@authService.canTransfer(#request)")
    public Receipt[] transferFunds(TransferFundsRequest request) {

        log.info("Starting transfer funds...");

        String maskedFromAccountNo = accountService
                .maskAccountNumber(request.getFromAccountNumber());

        String maskedToAccountNo = accountService
                .maskAccountNumber(request.getToAccountNumber());

        String outDescription = String.format("%s%s", String.format("TRANSFER to account %s", maskedToAccountNo),
                (Strings.isNotBlank(request.getMemo()) && request.getMemo() != null) ? " - " + request.getMemo() : "");

        String inDescription = String.format("%s%s", String.format("TRANSFER from account %s", maskedFromAccountNo),
                (Strings.isNotBlank(request.getMemo()) && request.getMemo() != null) ? " - " + request.getMemo() : "");

        CreateTransaction transferOut = CreateTransaction.builder()
                .accountNumber(request.getFromAccountNumber())
                .type(TransactionType.TRANSFER_OUT)
                .amount(request.getAmount())
                .description(outDescription)
                .method(TransactionMethod.APP)
                .date(request.getDate())
                .build();

        CreateTransaction transferIn = CreateTransaction.builder()
                .accountNumber(request.getToAccountNumber())
                .type(TransactionType.TRANSFER_IN)
                .amount(request.getAmount())
                .description(inDescription)
                .method(TransactionMethod.APP)
                .date(request.getDate())
                .build();

        Transaction outTransaction = createTransaction(transferOut);
        Transaction inTransaction = createTransaction(transferIn);

        log.info("Creating transfer transactions: [out: {}, in: {}]", outTransaction.getId(), inTransaction.getId());

        Receipt outReceipt = processTransaction(outTransaction);
        Receipt inReceipt;

        Transaction processedOutTransaction = repository.findById(outReceipt.getId())
                .orElseThrow(TransactionNotFoundException::new);

        if (processedOutTransaction.getStatus() == TransactionStatus.DENIED) {
            inTransaction.setState(TransactionState.PROCESSING);
            denyTransaction(inTransaction);
            postTransaction(inTransaction);
            inReceipt = mapToReceipt(inTransaction);
            inReceipt.setStatus(TransactionStatus.DENIED);
        } else {
            inReceipt = processTransaction(inTransaction);
        }

        log.info("Transfer transactions [out: {}, in: {}, amount: {}] {}",
                outTransaction.getId(),
                inTransaction.getId(),
                outTransaction.getAmount(),
                inTransaction.getStatus());

        return new Receipt[]{
                outReceipt,
                inReceipt
        };

    }

}
