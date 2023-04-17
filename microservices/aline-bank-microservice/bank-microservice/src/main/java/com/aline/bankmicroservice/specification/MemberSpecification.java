package com.aline.bankmicroservice.specification;

import com.aline.bankmicroservice.dto.request.MemberSearchCriteria;
import com.aline.core.model.Applicant;
import com.aline.core.model.Applicant_;
import com.aline.core.model.Member;
import com.aline.core.model.Member_;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.Account_;
import com.aline.core.model.account.CheckingAccount;
import com.aline.core.model.account.SavingsAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import java.text.MessageFormat;
import java.util.Locale;

@Slf4j(topic = "Member Specifications")
public class MemberSpecification {

    public static Specification<Member> nameContains(String name) {
        if (name == null || name.length() <= 0) {
            return null;
        }
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<Member, Applicant> memberApplicantJoin = root.join(Member_.applicant);
            return criteriaBuilder.or(
                    criteriaBuilder.like(memberApplicantJoin.get(Applicant_.FIRST_NAME), contains(name)),
                    criteriaBuilder.like(memberApplicantJoin.get(Applicant_.MIDDLE_NAME), contains(name)),
                    criteriaBuilder.like(memberApplicantJoin.get(Applicant_.LAST_NAME), contains(name))
            );
        };
    }

    public static Specification<Member> membershipOrApplicantIdContains(Long id) {
        if (id == null) return null;
        return ((root, criteriaQuery, criteriaBuilder) -> {
            Join<Member, Applicant> memberApplicantJoin = root.join(Member_.applicant);

            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Member_.MEMBERSHIP_ID), contains(String.valueOf(id))),
                    criteriaBuilder.like(memberApplicantJoin.get(Applicant_.ID).as(String.class), contains(String.valueOf(id)))
            );
        });
    }

    public static Specification<Member> membersWithAccountStatus(String accountStatus) {
        if (accountStatus == null || accountStatus.length() <= 0) {
            return null;
        }
        return ((root, cq, cb) -> {
            Join<Member, Account> accountJoin = root.join(Member_.accounts);
            return cb.equal(accountJoin.get(Account_.STATUS).as(String.class), accountStatus.toUpperCase(Locale.ROOT));
        });
    }

    public static Specification<Member> membersAsPrimaryAccountHolder() {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Join<Member, Account> memberAccountJoin = root.join(Member_.accounts);
            Path<Member> primaryHolder = memberAccountJoin.join(Account_.primaryAccountHolder).get(Member_.MEMBERSHIP_ID);
            return criteriaBuilder.equal(primaryHolder, root.get(Member_.MEMBERSHIP_ID));
        }
        );
    }

    public static Specification<Member> membersWithCheckingAccounts() {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            Join<Member, Account> memberAccountJoin = root.join(Member_.accounts);
            return criteriaBuilder.equal(
                    memberAccountJoin.type(), criteriaBuilder.literal(CheckingAccount.class)
            );
        });
    }

    public static Specification<Member> membersWithSavingsAccounts() {
        return (((root, criteriaQuery, criteriaBuilder) -> {
            Join<Member, Account> memberAccountJoin = root.join(Member_.accounts);
            return criteriaBuilder.equal(
                    memberAccountJoin.type(), criteriaBuilder.literal(SavingsAccount.class)
            );
        }));
    }

    public static Specification<Member> membersWithAccountsBalanceGreaterThanOrEqualTo(int startingAmt) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            Join<Member, Account> memberAccountJoin = root.join(Member_.accounts);
            Path<Member> accountBalance = memberAccountJoin.get(Account_.BALANCE);
            return criteriaBuilder.greaterThanOrEqualTo(accountBalance.as(Integer.class), startingAmt);
        });
    }

    public static Specification<Member> membersWithAccountsBalanceLesserThanOrEqualTo(int endingAmt) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            Join<Member, Account> memberAccountJoin = root.join(Member_.accounts);
            Path<Member> accountBalance = memberAccountJoin.get(Account_.BALANCE);
            return criteriaBuilder.lessThanOrEqualTo(accountBalance.as(Integer.class), endingAmt);
        });
    }

    public static Specification<Member> memberSearch(MemberSearchCriteria searchCriteria) {
        Specification<Member> searchSpecification = Specification.where(null);

        if (searchCriteria == null) {
            return searchSpecification;
        }

        if (searchCriteria.getSearchName() != null) {
            searchSpecification = searchSpecification.and(nameContains(searchCriteria.getSearchName()));
        }
        if (searchCriteria.getSearchId() != null) {
            searchSpecification = searchSpecification.and(membershipOrApplicantIdContains(searchCriteria.getSearchId()));
        }
        if (searchCriteria.getAccountStatus() != null) {
            searchSpecification = searchSpecification.and(membersWithAccountStatus(searchCriteria.getAccountStatus()));
        }
        if (searchCriteria.getIsPrimary() != null && searchCriteria.getIsPrimary()) {
            searchSpecification = searchSpecification.and(membersAsPrimaryAccountHolder());
        }
        if (searchCriteria.getHasChecking() != null & Boolean.TRUE.equals(searchCriteria.getHasChecking())) {
            searchSpecification = searchSpecification.and(membersWithCheckingAccounts());
        }
        if (searchCriteria.getHasSavings() != null & Boolean.TRUE.equals(searchCriteria.getHasSavings())) {
            searchSpecification = searchSpecification.and(membersWithSavingsAccounts());
        }

        return searchSpecification;
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
