package com.aline.core.repository;

import com.aline.core.model.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends IUserRepository<User>, JpaRepositoryWithSpecification<User, Long> {
}
