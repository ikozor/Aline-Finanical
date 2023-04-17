package com.aline.cardmicroservice.repository;

import com.aline.core.model.card.CardIssuer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardIssuerRepository extends JpaRepository<CardIssuer, String> {
    Optional<CardIssuer> findByIssuerName(String s);
}
