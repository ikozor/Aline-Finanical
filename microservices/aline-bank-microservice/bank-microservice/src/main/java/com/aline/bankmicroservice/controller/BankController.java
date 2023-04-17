package com.aline.bankmicroservice.controller;

import com.aline.bankmicroservice.dto.request.CreateBank;
import com.aline.bankmicroservice.service.BankService;
import com.aline.core.model.Bank;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/banks")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Bank")
public class BankController {
    private final BankService bankService;
    @Value("${server.port}")
    public int PORT;

    @GetMapping
    public ResponseEntity<Page<Bank>> getBanks(Pageable pageable) {
        Page<Bank> bankPage = bankService.getBanks(pageable);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(bankPage);
    }

    @GetMapping(value = {"/routing/{id}", "/routing"})
    public ResponseEntity<String> getBankRoutingNumber(@PathVariable(required = false) Long id) {
        String routingNum = bankService.getBankRouting(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(routingNum);
    }

    @GetMapping(value = {"/id/{bankId}", "/id"})
    public ResponseEntity<Bank> getBankById(@PathVariable(required = false) Long bankId) {
        Bank bank = bankService.findBankById(bankId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(bank);
    }

    @PostMapping
    public ResponseEntity<Bank> createBank(@RequestBody CreateBank bankDetails) {
        Bank bank = bankService.createBank(bankDetails);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/bank/{id}")
                .port(PORT)
                .buildAndExpand(bank.getId())
                .toUri();

        return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).body(bank);
    }

}
