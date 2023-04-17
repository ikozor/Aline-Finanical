package com.aline.transactionmicroservice.repository;

import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.repository.JpaRepositoryWithSpecification;
import com.aline.transactionmicroservice.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Transaction repository
 */
@Repository
public interface TransactionRepository extends JpaRepositoryWithSpecification<Transaction, Long> {

    /**
     * Find all transactions by the account
     * @param account The account that is associated with all the transactions
     * @param pageable Pageable object for a page response
     * @return A transactions page
     */
    Page<Transaction> findAllByAccount(Account account, Pageable pageable);

    /**
     * Find all transactions associated with an account
     * that a member is associated with.
     * @param member The member to query
     * @return A transactions page
     */
    @Query("SELECT t FROM Transaction t JOIN t.account.members m WHERE m = ?1")
    Page<Transaction> findAllByMember(Member member, Pageable pageable);
}
