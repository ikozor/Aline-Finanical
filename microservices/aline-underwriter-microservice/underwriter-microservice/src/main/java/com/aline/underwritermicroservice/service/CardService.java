package com.aline.underwritermicroservice.service;

import com.aline.core.aws.email.EmailService;
import com.aline.core.config.AppConfig;
import com.aline.core.dto.response.CardResponse;
import com.aline.core.model.Applicant;
import com.aline.core.model.Application;
import com.aline.core.model.Member;
import com.aline.core.model.account.CreditCardAccount;
import com.aline.core.model.card.Card;
import com.aline.core.model.card.CardIssuer;
import com.aline.core.model.card.CardStatus;
import com.aline.core.model.card.CardType;
import com.aline.core.model.card.IssuerIdentificationNumber;
import com.aline.core.model.credit.CreditCardOffer;
import com.aline.core.util.CardUtility;
import com.aline.core.util.RandomNumberGenerator;
import com.aline.underwritermicroservice.repository.CardRepository;
import com.aline.underwritermicroservice.repository.IssuerIdentificationNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final AppConfig appConfig;
    private final EmailService emailService;
    private final CardUtility cardUtility;
    private final RandomNumberGenerator randomNumberGenerator;
    private final IssuerIdentificationNumberRepository iinRepository;
    private final CardRepository cardRepository;

    public String generateCardNumber(String iin, int cardNumberLength) {
        return cardUtility.generateCardNumber(iin, cardNumberLength);
    }

    public List<IssuerIdentificationNumber> getIInsByIssuer(CardIssuer issuer) {
        return iinRepository.getIssuerIdentificationNumbersByCardIssuer(issuer);
    }

    public CardResponse mapToResponse(Card card) {
        return cardUtility.mapToResponse(card);
    }

    public boolean validateCardNumber(String cardNumber) {
        return cardUtility.validateCardNumber(cardNumber);
    }

    public Card createCard(Application application, CreditCardAccount account) {

        CreditCardOffer offer = application.getCardOffer();
        CardIssuer issuer = offer.getCardIssuer();
        String iin = getIin(getIInsByIssuer(issuer));

        LocalDate expirationDate = LocalDate.now()
                .withDayOfMonth(1)
                .plusYears(3);

        Card card = Card.builder()
                .account(account)
                .cardHolder(account.getPrimaryAccountHolder())
                .cardStatus(CardStatus.INACTIVE)
                .expirationDate(expirationDate)
                .cardNumber(generateCardNumber(iin, issuer.getCardNumberLength()))
                .securityCode(randomNumberGenerator.generateRandomNumberString(3))
                .cardType(CardType.CREDIT)
                .cardIssuer(issuer)
                .build();

        return cardRepository.save(card);

    }

    public void sendCard(Card card, boolean replacement) {
        Member member = card.getCardHolder();
        Applicant applicant = member.getApplicant();
        CardResponse cardResponse = cardUtility.mapToResponse(card);
        String cardNumber = cardResponse.getCardNumber();
        String securityCode = cardResponse.getSecurityCode();
        LocalDate expirationDate = cardResponse.getExpirationDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        String cardHolderName = cardResponse.getCardHolder().toUpperCase();
        String memberDashboard = appConfig.getMemberDashboard() + "/activate";

        String formattedCardNumber = cardNumber.replaceAll("\\d{4}(?!$)", "$0 ");

        Map<String, String> variables = Arrays.stream(new String[][] {
                {"name", applicant.getFirstName()},
                {"landingPortalUrl", appConfig.getLandingPortal()},
                {"cardNumber", formattedCardNumber},
                {"securityCode", securityCode},
                {"expirationDate", expirationDate.format(formatter)},
                {"cardHolderName", cardHolderName},
                {"activateCardUrl", memberDashboard},
                {"issuerName", card.getCardIssuer().getIssuerName()}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        String templateName = replacement ? "card/replace-card" : "card/send-card";

        //emailService.sendHtmlEmail("Credit card successfully issued", templateName, applicant.getEmail(), variables);
    }

    private String getIin(List<IssuerIdentificationNumber> iinList) {
        if (iinList.isEmpty())
            return null;

        if (iinList.size() == 1)
            return iinList.get(0).getIin();

        Random random = new Random();
        return iinList.get(random.nextInt(iinList.size())).getIin();

    }
}
