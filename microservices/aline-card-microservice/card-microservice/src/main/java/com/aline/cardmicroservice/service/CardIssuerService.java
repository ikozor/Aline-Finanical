package com.aline.cardmicroservice.service;

import com.aline.cardmicroservice.repository.CardIssuerRepository;
import com.aline.cardmicroservice.repository.IssuerIdentificationNumberRepository;
import com.aline.core.config.AppConfig;
import com.aline.core.exception.notfound.CardIssuerNotFound;
import com.aline.core.model.card.CardIssuer;
import com.aline.core.model.card.IssuerIdentificationNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardIssuerService {

    private final AppConfig appConfig;
    private final CardIssuerRepository cardIssuerRepository;
    private final IssuerIdentificationNumberRepository iinRepository;

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(appConfig.getDefaultCardIssuer())) {
            log.error("Application configuration does not declare default card issuer. CardIssuerService requires this property to be set.");
        }
    }

    public CardIssuer getCardIssuerByName(String issuerName) {
        return cardIssuerRepository.findByIssuerName(issuerName)
                .orElseThrow(CardIssuerNotFound::new);
    }
    public IssuerIdentificationNumber getIinByIssuerName(String issuerName) {
        return iinRepository.findIssuerIdentificationNumberByCardIssuerIssuerName(issuerName)
                .orElseThrow(CardIssuerNotFound::new);
    }

    public CardIssuer getDefaultCardIssuer() {
        return getCardIssuerByName(appConfig.getDefaultCardIssuer());
    }

    public IssuerIdentificationNumber getDefaultIin() {
        return getIinByIssuerName(appConfig.getDefaultCardIssuer());
    }

}
