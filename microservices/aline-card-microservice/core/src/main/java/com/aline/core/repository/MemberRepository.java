package com.aline.core.repository;

import com.aline.core.model.Applicant;
import com.aline.core.model.Member;
import com.aline.core.repository.JpaRepositoryWithSpecification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepositoryWithSpecification<Member, Long> {
    Optional<Member> findByMembershipId(String membershipId);
    Optional<Member> findMemberByApplicant(Applicant applicant);
    boolean existsMemberByApplicant(Applicant applicant);
}
