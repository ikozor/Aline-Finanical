package com.aline.cardmicroservice.controller;

import com.aline.core.dto.request.ActivateCardRequest;
import com.aline.core.dto.response.CardResponse;
import com.aline.core.dto.request.CreateDebitCardRequest;
import com.aline.core.dto.response.CreateDebitCardResponse;
import com.aline.cardmicroservice.service.CardEmailService;
import com.aline.cardmicroservice.service.CardService;
import com.aline.core.model.card.Card;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;
    private final CardEmailService cardEmailService;

    @GetMapping("/{id}")
    public CardResponse getCardById(@PathVariable Long id) {
        return cardService.mapToResponse(cardService.getCardById(id));
    }

    @PostMapping("/debit")
    public CreateDebitCardResponse createDebitCard(@RequestBody @Valid CreateDebitCardRequest request) {
        Card card = cardService.createDebitCard(request);
        log.info("Successfully created debit card.");
        log.info("Sending card to member...");
        //cardEmailService.sendCard(card, request.isReplacement());
        log.info("Successfully sent card to member.");
        CardResponse cardResponse = cardService.mapToResponse(card);
        return CreateDebitCardResponse.builder()
                .cardNumber(card.getCardNumber())
                .securityCode(card.getSecurityCode())
                .expirationDate(card.getExpirationDate())
                .cardHolder(cardResponse.getCardHolder())
                .cardHolderId(card.getCardHolder().getMembershipId())
                .accountNumber(card.getAccount().getAccountNumber())
                .build();
    }

    @PostMapping("/activation")
    public CardResponse activateCard(@Valid @RequestBody ActivateCardRequest cardRequest) {
        return cardService.mapToResponse(cardService.activateCard(cardRequest));
    }

}
