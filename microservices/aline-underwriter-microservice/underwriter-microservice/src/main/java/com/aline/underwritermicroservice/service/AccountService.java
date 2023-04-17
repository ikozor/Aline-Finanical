package com.aline.underwritermicroservice.service;

import com.aline.core.dto.response.CardResponse;
import com.aline.core.exception.BadRequestException;
import com.aline.core.exception.notfound.AccountNotFoundException;
import com.aline.core.model.Application;
import com.aline.core.model.ApplicationType;
import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountStatus;
import com.aline.core.model.account.CheckingAccount;
import com.aline.core.model.account.CreditCardAccount;
import com.aline.core.model.account.LoanAccount;
import com.aline.core.model.account.SavingsAccount;
import com.aline.core.model.card.Card;
import com.aline.core.model.card.CardStatus;
import com.aline.core.model.credit.CreditLine;
import com.aline.core.model.credit.CreditLineStatus;
import com.aline.core.model.loan.Loan;
import com.aline.core.model.loan.LoanStatus;
import com.aline.core.model.payment.Payment;
import com.aline.core.model.payment.PaymentStatus;
import com.aline.core.repository.AccountRepository;
import com.aline.core.repository.LoanRepository;
import com.aline.core.repository.PaymentRepository;
import com.aline.core.util.CardUtility;
import com.aline.underwritermicroservice.repository.CreditLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Account Service
 * <p>
 *     Used to create an account in the context of
 *     approving an application.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final CreditLineRepository creditLineRepository;
    private final UnderwriterService underwriterService;
    private final CardService cardService;

    /**
     * Create a single or multiple accounts based on the applicationType
     * @param application See {@link Application} to see what kinds of accounts can be created.
     * @param primaryAccountHolder The primary member.
     * @param members The members attached to the account including the primary member.
     * @return A set of accounts that were created.
     */
    @Transactional
    public Set<Account> createAccount(Application application, Member primaryAccountHolder, Set<Member> members) {
        Set<Account> accounts = new HashSet<>();
        switch (application.getApplicationType()) {
            case CHECKING:
                accounts.add(createCheckingAccount(primaryAccountHolder, members));
                break;
            case SAVINGS:
                accounts.add(createSavingsAccount(primaryAccountHolder, members));
                break;
            case CHECKING_AND_SAVINGS:
                accounts.add(createCheckingAccount(primaryAccountHolder, members));
                accounts.add(createSavingsAccount(primaryAccountHolder, members));
                break;
            case LOAN:
                accounts.add(createLoan(application, primaryAccountHolder, members));
                break;
            case CREDIT_CARD:
                accounts.add(createCreditCard(application, primaryAccountHolder, members));
            default:
                break;
        }

        return accounts;
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
    }

    private Account createCheckingAccount(Member primaryAccountHolder, Set<Member> members) {
        CheckingAccount account = CheckingAccount.builder()
                .primaryAccountHolder(primaryAccountHolder)
                .balance(0)
                .availableBalance(0)
                .status(AccountStatus.ACTIVE)
                .members(members)
                .build();
        return Optional.of(repository.save(account)).orElseThrow(() -> new BadRequestException("Account was not saved."));
    }

    private Account createSavingsAccount(Member primaryAccountHolder, Set<Member> members) {
        SavingsAccount account = SavingsAccount.builder()
                .primaryAccountHolder(primaryAccountHolder)
                .balance(0)
                .apy(0.006f)
                .members(members)
                .status(AccountStatus.ACTIVE)
                .build();
        return Optional.of(repository.save(account)).orElseThrow(() -> new BadRequestException("Account was not saved."));
    }


    @Transactional
    public Account createLoan(Application application, Member primaryPayer, Set<Member> payers) {
        if (application.getApplicationType() != ApplicationType.LOAN)
            throw new BadRequestException("Attempting to create a loan with an application type that does not include a loan.");

        Account depositAccount = application.getDepositAccount();

        Loan loan = underwriterService.createLoan(application);
        loan.setStatus(LoanStatus.OPEN);
        loan.setDepositAccount(depositAccount);
        LoanAccount account = LoanAccount.builder()
                .primaryAccountHolder(primaryPayer)
                .balance(0)
                .loan(loanRepository.save(loan))
                .members(payers)
                .payments(new ArrayList<>())
                .paymentHistory(new ArrayList<>())
                .status(AccountStatus.ACTIVE)
                .build();

        int term = loan.getTerm();
        float apr = loan.getApr();
        int amount = loan.getAmount() + Math.round(loan.getAmount() * (apr/100f));
        LocalDate startDate = loan.getStartDate();
        int month = startDate.getMonthValue();
        int day = Math.min(startDate.getDayOfMonth(), 28);
        int year = startDate.getYear();
        startDate = LocalDate.of(year, month, day);

        LoanAccount loanAccount = Optional.of(repository.save(account)).orElseThrow(() -> new BadRequestException("Account was not saved."));

        List<Payment> payments = getPaymentAmountsList(amount, term, new ArrayList<>(), loanAccount, startDate);
        loanAccount.setPayments(paymentRepository.saveAll(payments));
        loanAccount.setPaymentHistory(new ArrayList<>());

        return loanAccount;
    }

    @Transactional
    public Account createCreditCard(Application application, Member primaryPayer, Set<Member> authorizedUsers) {
        if (application.getApplicationType() != ApplicationType.CREDIT_CARD)
            throw new BadRequestException(String.format("Cannot create credit line with application type: %s", application.getApplicationType().name()));

        if (application.getCardOffer() == null)
            throw new BadRequestException("Card offer is required if application type is CREDIT_CARD.");

        CreditLine creditLine = underwriterService.createCreditLine(application);
        creditLine.setStatus(CreditLineStatus.OPEN);

        CreditCardAccount account = CreditCardAccount.builder()
                .primaryAccountHolder(primaryPayer)
                .balance(0)
                .members(authorizedUsers)
                .status(AccountStatus.ACTIVE)
                .creditLine(creditLineRepository.save(creditLine))
                .payments(new ArrayList<>())
                .paymentHistory(new ArrayList<>())
                .cards(new HashSet<>())
                .build();

        CreditCardAccount creditCardAccount = repository.save(account);
        Card creditCard = cardService.createCard(application, creditCardAccount);
        cardService.sendCard(creditCard,false);

        return creditCardAccount;

    }

    public List<Payment> getPaymentAmountsList(int amount, int term, List<Payment> payments, LoanAccount loanAccount, LocalDate paymentDate) {
        if (amount <= 0 || term == 0) return payments;
        int paymentAmount = amount / term;
        paymentDate = paymentDate.plusMonths(1);
        Payment payment = Payment.builder()
                .amount(paymentAmount)
                .payer(loanAccount.getPrimaryAccountHolder())
                .status(PaymentStatus.PENDING)
                .dueDate(paymentDate)
                .description(String.format("Loan payment %s", paymentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))))
                .payToAccount(loanAccount)
                .build();
        payments.add(payment);
        term--;
        amount -= paymentAmount;

        return getPaymentAmountsList(amount, term, payments, loanAccount, paymentDate);
    }

}
