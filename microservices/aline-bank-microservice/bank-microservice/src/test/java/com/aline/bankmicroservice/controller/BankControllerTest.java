package com.aline.bankmicroservice.controller;

import com.aline.bankmicroservice.dto.request.CreateBank;
import com.aline.core.model.Bank;
import com.aline.core.repository.BankRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Bank controller Integration test")
@Slf4j(topic = "Bank controller test")
class BankControllerTest {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    MockMvc mock;

    @BeforeEach
    void setUp() {
        List<Bank> banks = Arrays.asList(
                Bank.builder()
                        .id(1L)
                        .routingNumber("125000000")
                        .address("12345 MyStreet Ave")
                        .city("MyCity")
                        .state("Washington")
                        .zipcode("55301")
                        .build(),
                Bank.builder()
                        .id(2L)
                        .routingNumber("125000001")
                        .address("12345 Second Ave")
                        .city("MyCity")
                        .state("Washington")
                        .zipcode("55302")
                        .build(),
                Bank.builder()
                        .id(3L)
                        .routingNumber("125000002")
                        .address("12345 Third Ave")
                        .city("MyCity")
                        .state("Washington")
                        .zipcode("55303")
                        .build()
        );
        bankRepository.saveAll(banks);
    }

/*
    @Test
    void getBanksPaginated() throws Exception {
        mock.perform(get("/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value("3"));
    }
*/

    @Test
    @WithAnonymousUser
    void getBankRoutingNumber_withDefaultValueOf1_and_StatusIsOk() throws Exception {
        mock.perform(get("/banks/routing"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("125000000"));
    }
/*
    @Test
    @WithAnonymousUser
    void getBankById_withDefaultValueOf1_and_StatusIsOk() throws Exception {
        mock.perform(get("/banks/id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }
*/
    @Test
    @WithMockUser(authorities ="employee")
    void createBank() throws Exception {

        mock.perform(post("/banks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"routingNumber\": \"125000004\", \"address\": \"1234 Fourth Ave\", " +
                        "\"city\": \"city\", \"state\": \"myState\", \"zipcode\": \"12345\"}")
        )
                .andExpect(status().isCreated());
    }
}
