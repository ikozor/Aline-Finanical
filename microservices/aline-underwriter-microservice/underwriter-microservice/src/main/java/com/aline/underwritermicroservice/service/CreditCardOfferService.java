package com.aline.underwritermicroservice.service;

import com.aline.core.model.credit.CreditCardOffer;
import com.aline.underwritermicroservice.exception.CreditCardOfferNotFoundException;
import com.aline.underwritermicroservice.repository.CreditCardOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditCardOfferService {

    private final CreditCardOfferRepository repository;

    public CreditCardOffer getOfferById(int id) {
        return repository.findById(id).orElseThrow(CreditCardOfferNotFoundException::new);
    }

}
