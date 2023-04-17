package com.aline.cardmicroservice.service;

import com.aline.cardmicroservice.repository.CardRepository;
import com.aline.core.dto.request.ActivateCardRequest;
import com.aline.core.dto.request.CardRequest;
import com.aline.core.dto.request.CreateDebitCardRequest;
import com.aline.core.dto.response.CardResponse;
import com.aline.core.exception.BadRequestException;
import com.aline.core.exception.ResponseEntityException;
import com.aline.core.exception.notfound.AccountNotFoundException;
import com.aline.core.exception.notfound.CardNotFoundException;
import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountStatus;
import com.aline.core.model.account.AccountType;
import com.aline.core.model.card.Card;
import com.aline.core.model.card.CardIssuer;
import com.aline.core.model.card.CardStatus;
import com.aline.core.model.card.CardType;
import com.aline.core.model.card.IssuerIdentificationNumber;
import com.aline.core.repository.AccountRepository;
import com.aline.core.util.CardUtility;
import com.aline.core.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository repository;
    private final AccountRepository accountRepository;
    private final CardIssuerService cardIssuerService;
    private final RandomNumberGenerator randomNumberGenerator;
    private final CardUtility cardUtility;

    @PostAuthorize("@authService.canAccess(#returnObject)")
    public Card getCardById(long id) {
        return repository.findById(id).orElseThrow(CardNotFoundException::new);
    }

    @PostAuthorize("@authService.canAccess(#returnObject)")
    public Card getCardByCardRequest(CardRequest cardRequest) {
        return repository.findByCardNumberAndSecurityCodeAndExpirationDate(
                cardRequest.getCardNumber(),
                cardRequest.getSecurityCode(),
                cardRequest.getExpirationDate()
        ).orElseThrow(CardNotFoundException::new);
    }

    @PreAuthorize("@authService.canAccessByMemberId(#memberId)")
    public List<Card> getCardsByMemberId(Long memberId) {
        return repository.getCardsByCardHolderId(memberId);
    }

    @PreAuthorize("@authService.canAccessByMemberId(#memberId)")
    public List<Card> getAvailableCardsByMemberId(Long memberId) {
        return getCardsByMemberId(memberId).stream()
                .filter(card -> card.getCardStatus() != CardStatus.CLOSED)
                .collect(Collectors.toList());
    }

    //@PreAuthorize("@authService.canAccessByCreateDebitCardRequest(#createDebitCardRequest)")
    //@Transactional(rollbackOn = ResponseEntityException.class)
    public Card createDebitCard(@Valid CreateDebitCardRequest createDebitCardRequest) {
        // One debit card per member per account
        String accountNumber = createDebitCardRequest.getAccountNumber();
        String membershipId = createDebitCardRequest.getMembershipId();

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);

        if (account.getAccountType() != AccountType.CHECKING)
            throw new BadRequestException("Debit cards can only be opened on a valid CHECKING account. This account is not a CHECKING account.");

        if (account.getStatus() == AccountStatus.INACTIVE)
            throw new BadRequestException("Cannot create a debit card on an inactive account.");

        if (account.getStatus() == AccountStatus.ARCHIVED)
            throw new BadRequestException("Cannot create a debit card on a closed account.");

        Member member = account.getMembers().stream()
                .filter(m -> m.getMembershipId().equals(membershipId))
                .findFirst()
                .orElseThrow(() -> {
                    throw new BadRequestException("Member does not exist in this account.");
                });

        boolean cardExists = repository.existsCardByCardHolderAndAccount(member, account);

        if (cardExists) {
            List<Card> allCards = repository.findCardsByCardHolderAndAccount(member, account);
            if (createDebitCardRequest.isReplacement()) {
                log.info("Requesting replacement. Closing all cards.");
                allCards.forEach(card -> card.setCardStatus(CardStatus.CLOSED));
                repository.saveAll(allCards);
                log.info("Proceeding with card creation...");
            } else {
                log.error("Activate card already exists. Please request a replacement");
                throw new BadRequestException("Active card already exists. Please request a replacement instead.");
            }
        }

        CardIssuer defaultCardIssuer = cardIssuerService.getDefaultCardIssuer();
        IssuerIdentificationNumber defaultIin = cardIssuerService.getDefaultIin();

        log.info("Using default issuer: {}", defaultCardIssuer.getIssuerName());

        String cardNumber = generateCardNumber(defaultIin.getIin(), defaultCardIssuer.getCardNumberLength());

        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setCardHolder(member);
        card.setAccount(account);
        card.setCardStatus(CardStatus.INACTIVE); // Default to inactive
        card.setCardType(CardType.DEBIT);
        card.setSecurityCode(randomNumberGenerator.generateRandomNumberString(3));
        LocalDate expDate = LocalDate.now()
                .minusDays(LocalDate.now().getDayOfMonth() - 1)
                .plusYears(3);
        card.setExpirationDate(expDate);

        Card savedCard = repository.save(card);

        if (account.getCards() == null) {
            account.setCards(new HashSet<>());
        }

        if (member.getCards() == null) {
            member.setCards(new HashSet<>());
        }

        account.getCards().add(savedCard);
        member.getCards().add(savedCard);

        log.info("Successfully saved card.");

        return savedCard;
    }

    public Card activateCard(@Valid ActivateCardRequest activateCardRequest) {

        Card card = getCardByCardRequest(CardRequest.builder()
                .cardNumber(activateCardRequest.getCardNumber())
                .securityCode(activateCardRequest.getSecurityCode())
                .expirationDate(activateCardRequest.getExpirationDate())
                .build());

        Member member = card.getCardHolder();
        String ssn = member.getApplicant().getSocialSecurity();

        LocalDate dateOfBirth = member.getApplicant().getDateOfBirth();
        String lastFourSSN = ssn.substring(ssn.length() - 4);

        // Verify cardholder information
        if (!dateOfBirth.isEqual(activateCardRequest.getDateOfBirth()) ||
            !lastFourSSN.equals(activateCardRequest.getLastFourOfSSN()))
            throw new BadRequestException("Information was not entered correctly. Please check your card and try again.");

        // Checks to allow card activation
        if (card.getCardStatus() == CardStatus.ACTIVE)
            throw new BadRequestException("Card is already active.");
        if (card.getCardStatus() == CardStatus.CLOSED)
            throw new BadRequestException("Card has been closed. Cannot activate a closed card.");
        if (card.getAccount().getStatus() == AccountStatus.ARCHIVED)
            throw new BadRequestException("Cannot activate a card on a closed account.");


        card.setCardStatus(CardStatus.ACTIVE);
        return repository.save(card);
    }

    public String generateCardNumber(String iin, int cardNumberLength) {
        return cardUtility.generateCardNumber(iin, cardNumberLength);
    }

    public CardResponse mapToResponse(Card card) {
        return cardUtility.mapToResponse(card);
    }

    public boolean validateCardNumber(String cardNumber) {
        return cardUtility.validateCardNumber(cardNumber);
    }
}
