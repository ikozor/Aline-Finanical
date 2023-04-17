package com.aline.cardmicroservice.repository;

import com.aline.core.model.card.IssuerIdentificationNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssuerIdentificationNumberRepository extends JpaRepository<IssuerIdentificationNumber, String> {
    Optional<IssuerIdentificationNumber> findIssuerIdentificationNumberByCardIssuerIssuerName(String name);
}
