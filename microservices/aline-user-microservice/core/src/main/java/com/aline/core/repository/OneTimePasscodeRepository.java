package com.aline.core.repository;

import com.aline.core.model.OneTimePasscode;
import com.aline.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface OneTimePasscodeRepository extends JpaRepository<OneTimePasscode, Integer> {

    Optional<OneTimePasscode> findByOtp(String otp);
    Optional<OneTimePasscode> findByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM OneTimePasscode u WHERE u.user.id = ?1")
    void deleteByUserId(long id);

    Optional<OneTimePasscode> findByUserUsername(String username);
}
