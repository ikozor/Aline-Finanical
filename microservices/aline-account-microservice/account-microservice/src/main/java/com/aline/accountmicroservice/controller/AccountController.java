package com.aline.accountmicroservice.controller;

import com.aline.accountmicroservice.service.AccountService;
import com.aline.core.dto.response.AccountResponse;
import com.aline.core.model.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse getAccountById(@PathVariable long id) {
        Account account = service.getAccountById(id);
        return service.mapToResponse(account);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = service.getAllAccounts(pageable);
        return service.mapToPaginatedResponse(accounts);
    }

}
