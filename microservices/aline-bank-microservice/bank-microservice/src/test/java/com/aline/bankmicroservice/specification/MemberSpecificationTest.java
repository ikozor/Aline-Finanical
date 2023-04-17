package com.aline.bankmicroservice.specification;

import com.aline.bankmicroservice.dto.request.MemberSearchCriteria;
import com.aline.core.model.*;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountStatus;
import com.aline.core.model.account.CheckingAccount;
import com.aline.core.model.account.SavingsAccount;
import com.aline.core.model.card.Card;
import com.aline.core.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Member Specification Test")
@Slf4j(topic = "Member Specification Test")
class MemberSpecificationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    Member testMember;
    Member testMemberTwo;

    @BeforeEach
    void setup() {
        Bank testBank = Bank.builder()
                .id(1L)
                .routingNumber("123456789")
                .address("12345 MyStreet Ave")
                .city("MyCity")
                .state("Washington")
                .zipcode("55301")
                .build();
        Branch testBranch = Branch.builder()
                .id(1L)
                .name("First Branch")
                .address("1111 Main St")
                .city("Denver")
                .state("Colorado")
                .zipcode("10101")
                .phone("(555) 555-5551")
                .bank(testBank)
                .build();
        Applicant testApplicant = Applicant.builder()
                .id(1L)
                .firstName("Marcus")
                .lastName("Burk")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .income(12300000)
                .email("test@mail.com")
                .phone("(123) 555-5551")
                .driversLicense("ASELW120")
                .address("1111 Main St")
                .city("MyCity")
                .state("Washington")
                .zipcode("12354")
                .mailingAddress("1111 Main St")
                .mailingCity("MyCity")
                .mailingState("Washington")
                .mailingZipcode("12354")
                .socialSecurity("111-11-1111")
                .build();
        Applicant testApplicantTwo = Applicant.builder()
                .id(2L)
                .firstName("Mark")
                .lastName("Daniels")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .income(12300000)
                .email("test1@mail.com")
                .phone("(123) 555-5560")
                .driversLicense("ASELW65410")
                .address("1111 Main St")
                .city("MyCity")
                .state("Washington")
                .zipcode("12354")
                .mailingAddress("1111 Main St")
                .mailingCity("MyCity")
                .mailingState("Washington")
                .mailingZipcode("12354")
                .socialSecurity("111-11-1112")
                .build();
        Applicant testApplicantThree = Applicant.builder()
                .id(3L)
                .firstName("Peter")
                .lastName("Burke")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .income(123000)
                .email("test3@mail.com")
                .phone("(123) 555-1021")
                .driversLicense("AASDF3516")
                .address("1111 Main St")
                .city("MyCity")
                .state("Washington")
                .zipcode("12354")
                .mailingAddress("1111 Main St")
                .mailingCity("MyCity")
                .mailingState("Washington")
                .mailingZipcode("12354")
                .socialSecurity("111-11-1113")
                .build();


        testApplicant = applicantRepository.save(testApplicant);

        Application testApplication = Application.builder()
                .applicationType(ApplicationType.CHECKING)
                .applicationStatus(ApplicationStatus.PENDING)
                .primaryApplicant(testApplicant)
                .build();
        testApplication = applicationRepository.save(testApplication);

        testApplicant.setApplications(Collections.singleton(testApplication));

        testApplicant = applicantRepository.save(testApplicant);

        List<Applicant> allApplicants = Arrays.asList(testApplicantTwo, testApplicantThree);

        bankRepository.save(testBank);
        applicantRepository.saveAll(allApplicants);

        Set<Account> accounts = new HashSet<>();
		Set<Card> cards = new HashSet<>();

        testMember = new Member(1L, testBranch, "1111", testApplicant, accounts, cards);
        testMemberTwo = new Member(2L, testBranch, "1112", testApplicantTwo, accounts, cards);
        Member testMemberThree = new Member(3L, testBranch, "2223", testApplicantThree, accounts, cards);
        testMember = memberRepository.save(testMember);
        testMemberTwo = memberRepository.save(testMemberTwo);

        SavingsAccount testSavingsAccount = SavingsAccount.builder()
                .status(AccountStatus.ACTIVE)
                .primaryAccountHolder(testMemberTwo)
                .balance(500000000)
                .build();

        CheckingAccount testAccount = CheckingAccount.builder()
                .primaryAccountHolder(testMember)
                .balance(3000000)
                .availableBalance(0)
                .status(AccountStatus.ACTIVE)
                .build();

        testAccount = accountRepository.save(testAccount);
        testSavingsAccount = accountRepository.save(testSavingsAccount);
        testMember.setApplicant(testApplicant);
        testMember.setAccounts(Collections.singleton(testAccount));
        testMemberThree.setAccounts(Collections.singleton(testSavingsAccount));

        List<Member> allMembers = Arrays.asList(testMember, testMemberTwo, testMemberThree);
        memberRepository.saveAll(allMembers);

    }
/*
    @Test
    void findMembers_By_Name() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Member> membersPage = memberRepository.findAll(Specification.where(MemberSpecification.nameContains("Mar")), pageable);

        log.info(String.valueOf(membersPage.getContent()));

        assertEquals(membersPage.getTotalElements(), 2);

    }
*/
    @Test
    void findMembers_With_ApplicantOrMemberIds_ThatContain_SearchId() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Member> membersPage = memberRepository.findAll(
                Specification.where(MemberSpecification.membershipOrApplicantIdContains(1L)),
                pageable
        );
        assertTrue(membersPage.getContent().size() >= 1);

    }

    @Test
    void findMembers_With_AccountStatus_Active() {

        List<Member> membersList = memberRepository.findAll(
                Specification.where(MemberSpecification.membersWithAccountStatus("ACTIVE"))
        );

        assertEquals(membersList.size(), 2);
    }

    @Test
    void findMembers_As_PrimaryAccountHolders() {
        List<Member> memberList = memberRepository.findAll(
                Specification.where(MemberSpecification.membersAsPrimaryAccountHolder())
        );

        assertEquals(memberList.size(), 1);
    }

    @Test
    void findMembers_With_CheckingAccounts() {
        List<Member> memberList = memberRepository.findAll(
                Specification.where(MemberSpecification.membersWithCheckingAccounts())
        );
        assertEquals(memberList.size(), 1);
    }

    @Test
    void findMembers_With_SavingsAccounts() {
        List<Member> memberList = memberRepository.findAll(
                Specification.where(MemberSpecification.membersWithSavingsAccounts())
        );
        assertEquals(memberList.size(), 1);
    }

    @Test
    void findMembers_With_AccountBalances_greaterThan_zero() {
        List<Member> memberList = memberRepository.findAll(
                Specification.where(MemberSpecification.membersWithAccountsBalanceGreaterThanOrEqualTo(0))
        );
        assertEquals(memberList.size(), 2);
    }

    @Test
    void findMembers_With_AccountBalances_lesserThan_amount() {
        List<Member> memberList = memberRepository.findAll(
                Specification.where(MemberSpecification.membersWithAccountsBalanceLesserThanOrEqualTo(5000000))
        );
        assertEquals(memberList.size(), 1);
    }

    @Test
    void findMembers_With_SearchCriteria() {
        MemberSearchCriteria searchCriteria = MemberSearchCriteria.builder()
                .searchId(null)
                .hasSavings(null)
                .accountStatus(null)
                .searchName("Mar")
                .hasChecking(Boolean.TRUE)
                .isPrimary(Boolean.TRUE)
                .build();

        List<Member> memberList = memberRepository.findAll(Specification.where(MemberSpecification.memberSearch(searchCriteria)));

        assertEquals(memberList.size(), 1);
    }


    @Test
    void findMembers_with_NullSearchCriteria() {

        MemberSearchCriteria searchCriteria = MemberSearchCriteria.builder()
                .searchName(null)
                .searchId(null)
                .hasSavings(null)
                .isPrimary(null)
                .hasChecking(null)
                .accountStatus(null)
                .build();

        List<Member> memberList = memberRepository.findAll(Specification.where(MemberSpecification.memberSearch(searchCriteria)));

        assertEquals(memberList.size(), 3);
    }
}
