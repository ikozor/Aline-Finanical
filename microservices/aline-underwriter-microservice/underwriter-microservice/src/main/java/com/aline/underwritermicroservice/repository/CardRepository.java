package com.aline.underwritermicroservice.repository;

import com.aline.core.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
