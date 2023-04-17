package com.aline.core.repository;

import com.aline.core.model.OneTimePasscode;
import com.aline.core.model.user.User;
import com.aline.core.model.user.UserRegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * IUserRepository is to be inherited by
 * other user repository interfaces.
 * @param <T> User model
 */
@NoRepositoryBean
public interface IUserRepository<T extends User> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    boolean existsByUsername(String username);

    Optional<T> findByUsername(String username);

    @Query("SELECT u FROM User u INNER JOIN UserRegistrationToken ut " +
            "ON u.id = ut.user.id " +
            "WHERE ut = ?1")
    Optional<T> findByToken(UserRegistrationToken token);

    @Query("SELECT u FROM User u INNER JOIN OneTimePasscode otp " +
            "ON u.id = otp.user.id " +
            "WHERE otp = ?1")
    Optional<T> findByOneTimePasscode(OneTimePasscode token);

}
