package com.aline.cardmicroservice.repository;

import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumberAndSecurityCodeAndExpirationDate(String cardNumber, String securityCode, LocalDate expirationDate);
    List<Card> findCardsByCardHolderAndAccount(Member cardHolder, Account account);
    boolean existsCardByCardHolderAndAccount(Member cardHolder, Account account);
    List<Card> getCardsByCardHolderId(Long cardHolderId);

}
