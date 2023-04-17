package com.aline.underwritermicroservice.repository;

import com.aline.core.model.credit.CreditLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditLineRepository extends JpaRepository<CreditLine, Integer> {
}
