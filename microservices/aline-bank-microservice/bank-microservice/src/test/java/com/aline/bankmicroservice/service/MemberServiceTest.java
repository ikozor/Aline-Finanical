package com.aline.bankmicroservice.service;

import com.aline.bankmicroservice.dto.response.MemberResponse;
import com.aline.core.model.*;
import com.aline.core.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Member Service Test")
@Slf4j(topic = "Member Service Test")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    Page<Member> testMemberPage;

    @BeforeEach
    void setUp() {
        Bank testBank = Bank.builder()
                .id(1L)
                .routingNumber("125000")
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

        Member testMember = new Member(1L, testBranch, "12345351635", testApplicant, new HashSet<>(), new HashSet<>());
        Member testMemberTwo = new Member(2L, testBranch, "7890351651", testApplicant, new HashSet<>(), new HashSet<>());
        Member testMemberThree = new Member(3L, testBranch, "98765321321", testApplicant, new HashSet<>(), new HashSet<>());

        Pageable pageable = PageRequest.of(0, 10);
        testMemberPage = new PageImpl<>(Arrays.asList(testMember, testMemberTwo, testMemberThree), pageable, 3);

    }

    @Test
    @WithMockUser(authorities = "employee")
    void SearchMembers_withoutCriteria_and_withPageable() {

        Pageable pageable = PageRequest.of(0, 10);
        Specification<Member> memberSpecification = Specification.where(null);

        when(memberRepository.findAll(memberSpecification, pageable)).thenReturn(testMemberPage);

        Page<MemberResponse> memberPaginatedResponse = memberService.searchMembers(null, pageable);

        assertEquals(memberPaginatedResponse.getTotalElements(), testMemberPage.getTotalElements());

    }


}
