package com.aline.core.repository;

import com.aline.core.model.user.MemberUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * User repository specifically for member users.
 * @apiNote This is mostly used for retrieval.
 * @see UserRepository See UserRepository for saving.
 */
@Repository
public interface MemberUserRepository extends IUserRepository<MemberUser> {

    @Query("SELECT CASE WHEN " +
            "COUNT(u) > 0 " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END FROM MemberUser u INNER JOIN Member m " +
            "ON u.member.id = m.id " +
            "WHERE m.membershipId = ?1")
    boolean existsByMembershipId(String membershipId);

}
