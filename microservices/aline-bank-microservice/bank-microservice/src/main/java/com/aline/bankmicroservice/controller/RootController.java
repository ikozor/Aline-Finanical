package com.aline.bankmicroservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Root")
@RestController
public class RootController {

    /**
     * Health Check endpoint for microservice
     * @return 200 Response if service is healthy, otherwise 404 is returned
     */
    @GetMapping("/health")
    @Operation(description = "Health Endpoint")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service is healthy."),
            @ApiResponse(responseCode = "404", description = "Service is unavailable")
    })
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
