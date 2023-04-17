package com.aline.underwritermicroservice.repository;

import com.aline.core.model.credit.CreditCardOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardOfferRepository extends JpaRepository<CreditCardOffer, Integer> {

}
