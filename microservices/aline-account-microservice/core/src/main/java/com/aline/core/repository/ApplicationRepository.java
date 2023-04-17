package com.aline.core.repository;

import com.aline.core.model.Application;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepositoryWithSpecification<Application, Long> {
}
