package com.aline.bankmicroservice.service;

import com.aline.bankmicroservice.dto.request.CreateBranch;
import com.aline.bankmicroservice.dto.request.UpdateBranch;
import com.aline.core.model.Bank;
import com.aline.core.model.Branch;
import com.aline.core.paginated.BranchPaginated;
import com.aline.core.repository.BankRepository;
import com.aline.core.repository.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Branch Service test")
@Slf4j(topic = "Branch Service test")
class BranchServiceTest {
    @Autowired
    BranchService branchService;

    @MockBean
    BranchRepository branchRepository;

    @MockBean
    BankRepository bankRepository;

    private List<Branch> branches;
    private BranchPaginated testPage;
    private Bank testBank;

    @BeforeEach
    void setUp() {
        testBank = Bank.builder()
                .id(1L)
                .routingNumber("125000")
                .address("12345 MyStreet Ave")
                .city("MyCity")
                .state("Washington")
                .zipcode("55301")
                .build();

        branches = Arrays.asList(
                Branch.builder()
                        .id(1L)
                        .name("First Branch")
                        .address("1111 Main St")
                        .city("Denver")
                        .state("Colorado")
                        .zipcode("10101")
                        .phone("(555) 555-5551")
                        .bank(testBank)
                        .build(),
                Branch.builder()
                        .id(2L)
                        .name("Second Branch")
                        .address("999 Main St")
                        .city("Sacramento")
                        .state("California")
                        .zipcode("10101")
                        .phone("(555) 555-5550")
                        .bank(testBank)
                        .build(),
                Branch.builder()
                        .id(3L)
                        .name("Third Branch")
                        .address("9990 Main St")
                        .city("Austin")
                        .state("Texas")
                        .zipcode("10101")
                        .phone("(555) 555-5552")
                        .bank(testBank)
                        .build()
        );

        Pageable pageable = PageRequest.of(0, 2);
        testPage = new BranchPaginated(branches, pageable, 3);

    }

    @Test
    void getPaginatedBranches() {
        Pageable pageable = PageRequest.of(0, 2);

        when(branchRepository.findAll(pageable)).thenReturn(testPage);

        BranchPaginated myTestPage = branchService.getBranches(pageable);

        assertEquals(branches, myTestPage.getContent());
        assertEquals(3, myTestPage.getContent().size());
        assertEquals(2, myTestPage.getSize());
    }

    @Test
    @WithMockUser(authorities = "employee")
    void postBranch() {
        CreateBranch testBranch = CreateBranch.builder()
                .name("New Branch")
                .address("1298 Seasame St")
                .city("New York City")
                .state("New York")
                .zipcode("12345")
                .phone("(123) 123-4567")
                .bankID(1L).build();

        when(bankRepository.findById(testBranch.getBankID())).thenReturn(Optional.of(testBank));

        branchService.postBranch(testBranch);

        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    @WithMockUser(username = "employee", authorities = "employee")
    void updateBranch() {
        UpdateBranch updateInfo = UpdateBranch.builder()
                .id(1L)
                .phone("(123) 123-4568")
                .bankID(1L).build();
        Branch myBranch = Branch.builder()
                .id(1L)
                .name("New Branch")
                .address("1298 Seasame St")
                .city("New York City")
                .state("New York")
                .zipcode("12345")
                .phone("(123) 123-4568")
                .bank(testBank).build();

        when(bankRepository.findById(updateInfo.getBankID())).thenReturn(Optional.of(testBank));
        when(branchRepository.findById(updateInfo.getId())).thenReturn(Optional.of(myBranch));

        branchService.updateBranch(updateInfo);

        verify(branchRepository).save(myBranch);

    }

    @Test
    @WithMockUser(username = "employee", authorities = "employee")
    void deleteBranch() {
        branchService.deleteBranch(1L);
        verify(branchRepository, times(1)).deleteById(1L);
    }
}
