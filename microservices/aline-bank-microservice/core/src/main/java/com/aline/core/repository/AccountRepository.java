package com.aline.core.repository;

import com.aline.core.model.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a " +
            "JOIN a.members m " +
            "WHERE m.id = ?1")
    Page<Account> findAllByMemberId(long memberId, Pageable pageable);

    /**
     * Find account by associated account number
     * @param accountNumber The account number
     * @return The account associated with that account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);
}
