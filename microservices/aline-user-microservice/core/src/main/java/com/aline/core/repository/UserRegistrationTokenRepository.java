package com.aline.core.repository;

import com.aline.core.model.user.UserRegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRegistrationTokenRepository extends JpaRepository<UserRegistrationToken, UUID> {

    /**
     * Find a token by a user ID.
     * @param id The id of the user to query.
     * @return An option of a registration token.
     */
    Optional<UserRegistrationToken> findByUserId(Long id);

}
