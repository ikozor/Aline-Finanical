package com.aline.core.repository;

import com.aline.core.model.Applicant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepositoryWithSpecification<Applicant, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByDriversLicense(String driversLicense);
    boolean existsBySocialSecurity(String socialSecurity);

    @Query("SELECT a FROM Applicant a " +
            "LEFT JOIN Member m " +
            "ON m.applicant = a " +
            "LEFT JOIN MemberUser u " +
            "ON u.member.id = m.id " +
            "WHERE u.username = ?1")
    Optional<Applicant> findApplicantByUsername(String username);

}
