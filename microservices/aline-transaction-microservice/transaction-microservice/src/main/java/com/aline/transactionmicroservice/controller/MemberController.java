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

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final TransactionService service;

    @GetMapping(value = "/{id}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<TransactionResponse> getAllTransactionsByMemberId(@PathVariable long id,
                                                                  Pageable pageable,
                                                                  @RequestParam(defaultValue = "")
                                                                  String[] search) {
        log.info("Get all transactions made by member {} with search term {}", id, search);
        Page<Transaction> transactions = service.getAllTransactionsByMemberId(id, pageable, search);
        return transactions.map(service::mapToResponse);
    }
}
