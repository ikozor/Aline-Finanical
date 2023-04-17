package com.aline.cardmicroservice.service;

import com.aline.core.dto.response.CardResponse;
import com.aline.core.aws.email.EmailService;
import com.aline.core.config.AppConfig;
import com.aline.core.model.Applicant;
import com.aline.core.model.Member;
import com.aline.core.model.card.Card;
import com.aline.core.util.CardUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardEmailService {

    private final EmailService emailService;
    private final AppConfig appConfig;
    private final CardUtility cardUtility;

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

        emailService.sendHtmlEmail("Card successfully issued", templateName, applicant.getEmail(), variables);
    }

}
