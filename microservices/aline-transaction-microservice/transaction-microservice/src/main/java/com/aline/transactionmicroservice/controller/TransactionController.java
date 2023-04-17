package com.aline.transactionmicroservice.controller;

import com.aline.transactionmicroservice.dto.CreateTransaction;
import com.aline.transactionmicroservice.dto.Receipt;
import com.aline.transactionmicroservice.dto.TransactionResponse;
import com.aline.transactionmicroservice.dto.TransferFundsRequest;
import com.aline.transactionmicroservice.model.Transaction;
import com.aline.transactionmicroservice.service.TransactionApi;
import com.aline.transactionmicroservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions API")
public class TransactionController {

    private final TransactionService service;
    private final TransactionApi transactions;

    @Operation(description = "Get transaction by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getTransactionById(@PathVariable long id) {
        Transaction transaction = service.getTransactionById(id);
        return service.mapToResponse(transaction);
    }

    @Operation(description = "Process a transaction normally. This endpoint creates, processes, validates, and posts a transaction all at once.")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Receipt processTransaction(@Valid @RequestBody CreateTransaction createTransaction) {
        Transaction transaction = transactions.createTransaction(createTransaction);
        return transactions.processTransaction(transaction);
    }

    @Operation(description = "Create a transfer funds transaction")
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public Receipt[] transferFunds(@Valid @RequestBody TransferFundsRequest transferFundsRequest) {
        return transactions.transferFunds(transferFundsRequest);
    }

}
