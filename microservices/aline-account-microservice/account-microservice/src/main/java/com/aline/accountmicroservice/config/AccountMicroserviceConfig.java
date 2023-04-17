package com.aline.accountmicroservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountMicroserviceConfig {

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

}
