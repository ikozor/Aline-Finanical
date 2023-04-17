package com.aline.bankmicroservice.controller;

import com.aline.core.model.Bank;
import com.aline.core.model.Branch;
import com.aline.core.repository.BankRepository;
import com.aline.core.repository.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for {@link BranchController}
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Branch controller Integration test")
@Slf4j(topic = "Branch controller test")
class BranchControllerTest {

    @Autowired
    MockMvc mock;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    BankRepository bankRepository;

    private static final String DEFAULT_PAGE_SIZE = "15";

    @BeforeEach
    void setUp() {
        Bank testBank = Bank.builder()
                .id(1L)
                .routingNumber("125000000")
                .address("12345 MyStreet Ave")
                .city("MyCity")
                .state("Washington")
                .zipcode("55301")
                .build();

        bankRepository.save(testBank);

        List<Branch> branches = Arrays.asList(
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
        branchRepository.saveAll(branches);
    }

/*
    @Test
    @WithAnonymousUser
    void getPaginatedBranchesResponse_withSizeAndPageParams_statusIsOk() throws Exception {
        mock.perform(get("/branches")
                .param("page", "0")
                .param("size", "2")

        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size").value("2"))
                .andExpect(jsonPath("$.['pageable'].['pageNumber']").value("0"));
    }

    @Test
    @WithMockUser(authorities = "employee")
    void getPaginatedBranchesResponse_withBadParametersUsesDefaults_statusIsOk() throws Exception {
        mock.perform(get("/branches")
                .param("page", "-1")
                .param("size", "-1")

        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size").value(DEFAULT_PAGE_SIZE))
                .andExpect(jsonPath("$.['pageable'].['pageNumber']").value("0"));
    }

    @Test
    @WithMockUser(authorities = "employee")
    void getPaginatedBranchesResponse_withNoUserInputsUsesDefaults_statusIsOk() throws Exception {
        mock.perform(get("/branches"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size").value(DEFAULT_PAGE_SIZE))
                .andExpect(jsonPath("$.['pageable'].['pageNumber']").value("0"));
    }
*/
}
