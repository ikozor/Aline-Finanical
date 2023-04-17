package com.aline.accountmicroservice;

import com.aline.core.annotation.test.SpringBootIntegrationTest;
import com.aline.core.annotation.test.SpringTestProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootIntegrationTest(SpringTestProperties.DISABLE_WEB_SECURITY)
@DisplayName("Account Microservice Integration Test")
@Sql(scripts = {"classpath:scripts/accounts.sql"})
@Transactional
public class AccountsIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void test_getAccountById_statusIsOk_and_containsAllCorrectInformation() throws Exception {

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balance").value(100000))
                .andExpect(jsonPath("$.availableBalance").value(100000))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.type").value("CHECKING"))
                .andDo(print());

    }

    @Test
    void test_getAccountById_statusIsNotFound_when_account_doesNotExist() throws Exception {

        mockMvc.perform(get("/accounts/999"))
                .andExpect(status().isNotFound());

    }

    @Test
    void test_getAccountsByMember_statusIsOk_when_accounts_exist() throws Exception {

        mockMvc.perform(get("/members/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andDo(print());

    }

}
