package com.aline.transactionmicroservice.repository;

import com.aline.core.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Optional<Card> findByCardNumberAndSecurityCodeAndExpirationDate(String cardNumber, String securityCode, LocalDate expirationDate);
}
