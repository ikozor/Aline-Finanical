package com.aline.transactionmicroservice.service;

import com.aline.core.dto.request.CardRequest;
import com.aline.core.exception.notfound.CardNotFoundException;
import com.aline.core.model.card.Card;
import com.aline.transactionmicroservice.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public Card getCardByCardRequest(CardRequest cardRequest) {
        return cardRepository.findByCardNumberAndSecurityCodeAndExpirationDate(
                cardRequest.getCardNumber(),
                cardRequest.getSecurityCode(),
                cardRequest.getExpirationDate()
        ).orElseThrow(CardNotFoundException::new);
    }

}
