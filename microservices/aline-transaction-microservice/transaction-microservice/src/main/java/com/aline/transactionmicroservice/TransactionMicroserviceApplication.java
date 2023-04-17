package com.aline.transactionmicroservice;

import com.aline.core.annotation.EnableCoreModule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableCoreModule
@OpenAPIDefinition(info =
    @Info(
            title = "Transaction Microservice",
            description = "Handle all account transaction",
            version = "1.0"
    )
)
@EntityScan
public class TransactionMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionMicroserviceApplication.class, args);
    }

}
