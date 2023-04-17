package com.aline.transactionmicroservice.repository;

import com.aline.core.repository.JpaRepositoryWithSpecification;
import com.aline.transactionmicroservice.model.Merchant;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepositoryWithSpecification<Merchant, String> {
}
