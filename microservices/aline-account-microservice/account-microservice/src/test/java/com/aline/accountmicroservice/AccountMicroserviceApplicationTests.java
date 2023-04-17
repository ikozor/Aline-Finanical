package com.aline.accountmicroservice;

import com.aline.core.annotation.test.SpringBootIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootIntegrationTest
class AccountMicroserviceApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountMicroserviceApplication application;

    @Test
    void contextLoads() {
        assertNotNull(application);
    }

    @Test
    void healthCheckStatusIsOK() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());
    }

}
