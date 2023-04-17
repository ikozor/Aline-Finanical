package com.aline.bankmicroservice;

import com.aline.core.annotation.EnableCoreModule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCoreModule
@OpenAPIDefinition( info =
        @Info(
                title = "Bank Microservice",
                description = "Microservice that manages bank and branches details and member search"
        )
)
public class BankMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankMicroserviceApplication.class, args);
    }

}
