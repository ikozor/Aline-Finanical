package com.aline.accountmicroservice.controller;

import com.aline.accountmicroservice.service.AccountService;
import com.aline.core.dto.response.AccountResponse;
import com.aline.core.model.account.Account;
import lombok.NonNull;
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
@RequestMapping("/members/{memberId}/accounts")
@RequiredArgsConstructor
public class MemberController {

    private final AccountService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<AccountResponse> getAllAccountsByMemberId(@NonNull Pageable pageable, @PathVariable long memberId) {
        Page<Account> accountPage = service.getAccountsByMemberId(memberId, pageable);
        return service.mapToPaginatedResponse(accountPage);
    }

}
