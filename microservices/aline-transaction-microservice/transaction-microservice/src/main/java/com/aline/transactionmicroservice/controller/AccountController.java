package com.aline.transactionmicroservice.controller;

import com.aline.transactionmicroservice.dto.TransactionResponse;
import com.aline.transactionmicroservice.model.Transaction;
import com.aline.transactionmicroservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final TransactionService service;

    @GetMapping(value = "/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<TransactionResponse> getAllTransactionsByAccountId(@PathVariable long id,
                                                                   Pageable pageable,
                                                                   @RequestParam(defaultValue = "")
                                                                   String[] search) {
        log.info("Get all transactions in account {} with search term {}", id, search);
        Page<Transaction> transactionsPage = service.getAllTransactionsByAccountId(id, pageable, search);
        return transactionsPage.map(service::mapToResponse);
    }

    @GetMapping(value = "/account-number/{accountNumber}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<TransactionResponse> getAllTransactionsByAccountId(@PathVariable String accountNumber,
                                                                   Pageable pageable,
                                                                   @RequestParam(defaultValue = "")
                                                                   String[] search) {
        Page<Transaction> transactionsPage = service.getAllTransactionsByAccountNumber(accountNumber, pageable, search);
        return transactionsPage.map(service::mapToResponse);
    }

}
