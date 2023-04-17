package com.aline.accountmicroservice;

import com.aline.core.annotation.EnableCoreModule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCoreModule
@OpenAPIDefinition(info =
    @Info(
            title = "Microservice Template",
            description = "(Update this description) This is a description of your microservice and it's functionality.",
            version = "1.0"
    )
)
public class AccountMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountMicroserviceApplication.class, args);
    }

}
