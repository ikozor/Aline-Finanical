package com.aline.transactionmicroservice;

import com.aline.core.annotation.test.SpringBootIntegrationTest;
import com.aline.core.annotation.test.SpringTestProperties;
import com.aline.transactionmicroservice.dto.TransferFundsRequest;
import com.aline.transactionmicroservice.exception.TransactionNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootIntegrationTest(SpringTestProperties.DISABLE_WEB_SECURITY)
@Sql(scripts = "classpath:scripts/transactions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class ApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TransactionMicroserviceApplication application;

    @Autowired
    ObjectMapper mapper;

    @Test
    void contextLoads() {
        assertNotNull(application);
    }

    @Test
    void healthCheckTest() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());
    }

    @Test
    void test_getTransactionById_status_isOk_when_transactionExists() throws Exception {
        mockMvc.perform(get("/transactions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("******1234"));
    }

    @Test
    void test_getTransactionById_status_isNotFound_when_transactionDoesNotExist() throws Exception {
        mockMvc.perform(get("/transactions/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().string((new TransactionNotFoundException()).getMessage()));
    }

    @Test
    void test_getAllTransactionsByAccountNumber_status_isOk() throws Exception {
        mockMvc.perform(get("/accounts/account-number/{accountNumber}/transactions",
                        "0011011234"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void test_getAllTransactionsByAccountId_status_isOk() throws Exception {
        mockMvc.perform(get("/accounts/{id}/transactions", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void test_getAllTransactionsByMemberId_status_isOk() throws Exception {
        mockMvc.perform(get("/members/{id}/transactions", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(5));
    }

    @Test
    void test_getAllTransactionsByMemberId_status_isOk_memberIsSpouse() throws Exception {
        mockMvc.perform(get("/members/{id}/transactions", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Nested
    @DisplayName("Search Transactions")
    class SearchTransactionsTest {

        @Test
        void test_searchTransactionsByAccountId_status_is_ok_correctAmount() throws Exception {

            mockMvc.perform(get("/accounts/{id}/transactions", 1)
                    .queryParam("search", "batman", "clark kent"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andDo(print());

        }

        @Test
        void test_searchTransactionsByMemberId_status_is_ok_correctAmount() throws Exception {
            mockMvc.perform(get("/members/{id}/transactions", 1)
                            .queryParam("search", "batman"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andDo(print());
        }

        @Test
        void test_searchTransactionsByAccountId_searchMerchantName_status_is_ok_correctAmount() throws Exception {
            mockMvc.perform(get("/accounts/{id}/transactions", 2)
                    .queryParam("search", "none"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andDo(print());
        }

        @Test
        void test_searchTransactionsByMemberId_searchWithMultiWordSearchTerm_status_is_ok_correctAmount() throws Exception {
            mockMvc.perform(get("/members/{id}/transactions", 1)
                    .queryParam("search", "is man"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(4))
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("Transfer Funds Test")
    class TransferFundsTests {

        @Test
        void test_transferFunds_statusIsOk_when_both_accountsExists() throws Exception {
            TransferFundsRequest request = TransferFundsRequest.builder()
                        .fromAccountNumber("0011011234")
                        .toAccountNumber("0012021234")
                        .amount(10000)
                        .build();

            String body = mapper.writeValueAsString(request);
            mockMvc.perform(post("/transactions/transfer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

    }

}
