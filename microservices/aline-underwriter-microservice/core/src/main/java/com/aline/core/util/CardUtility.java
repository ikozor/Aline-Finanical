package com.aline.core.util;

import com.aline.core.dto.response.CardResponse;
import com.aline.core.model.Applicant;
import com.aline.core.model.card.Card;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardUtility {

    private final RandomNumberGenerator randomNumberGenerator;

    public String generateCardNumber(String iin, int length) {
        int numsToGenerate = length - iin.length() - 1;
        String randomNumbers = randomNumberGenerator.generateRandomNumberString(numsToGenerate);
        StringBuilder cardNumberBuilder = new StringBuilder();
        cardNumberBuilder.append(iin);
        cardNumberBuilder.append(randomNumbers);
        int checkDigit = 10 - (getCheckSum(cardNumberBuilder.toString()) % 10);
        return cardNumberBuilder.append(checkDigit).toString();
    }

    private int getCheckSum(String partCardNo) {
        int len = partCardNo.length();
        int checkSum = 0;
        boolean isOdd = true;
        for (int i = len - 1; i >= 0; i--) {
            int digit = partCardNo.charAt(i) - '0';
            if (isOdd) {
                digit *= 2;
                if (digit > 9)
                    digit -= 9;
            }
            checkSum += digit;
            isOdd = !isOdd;
        }
        return checkSum;
    }

    public boolean validateCardNumber(String cardNumber) {
        int checkDigit = cardNumber.charAt(cardNumber.length() - 1) - '0';
        int checkSum = getCheckSum(cardNumber.substring(0, cardNumber.length() - 1));
        return (checkSum + checkDigit) % 10 == 0;
    }

    public CardResponse mapToResponse(Card card) {

        Applicant applicant = card.getCardHolder().getApplicant();
        StringBuilder cardHolderNameBuilder = new StringBuilder();
        cardHolderNameBuilder.append(applicant.getFirstName()).append(" ");
        if (StringUtils.isNotEmpty(applicant.getMiddleName())) {
            cardHolderNameBuilder.append(applicant.getMiddleName()).append(" ");
        }
        cardHolderNameBuilder.append(applicant.getLastName());
        String cardHolderName = cardHolderNameBuilder.toString();

        return CardResponse.builder()
                .cardNumber(card.getCardNumber())
                .securityCode(card.getSecurityCode())
                .expirationDate(card.getExpirationDate())
                .cardHolder(cardHolderName)
                .cardStatus(card.getCardStatus().name())
                .build();
    }

}
