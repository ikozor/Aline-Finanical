package com.aline.underwritermicroservice.repository;

import com.aline.core.model.card.CardIssuer;
import com.aline.core.model.card.IssuerIdentificationNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuerIdentificationNumberRepository extends JpaRepository<IssuerIdentificationNumber, Integer> {

    List<IssuerIdentificationNumber> getIssuerIdentificationNumbersByCardIssuer(CardIssuer cardIssuer);

}
