package com.aline.accountmicroservice.service;

import com.aline.core.dto.response.AccountResponse;
import com.aline.core.dto.response.PaginatedResponse;
import com.aline.core.exception.notfound.AccountNotFoundException;
import com.aline.core.model.account.Account;
import com.aline.core.repository.AccountRepository;
import com.aline.core.security.annotation.PermitAll;
import com.aline.core.security.annotation.RoleIsManagement;
import com.aline.core.util.SimpleSearchSpecification;
import com.aline.core.validation.annotation.MembershipId;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final ModelMapper mapper;

    /**
     * Get an account by its primary key.
     * @param id The primary key of the account
     * @return The account with the specified primary key.
     */
    @PermitAll
    @PostAuthorize("@accountAuth.canAccess(returnObject)")
    public Account getAccountById(long id) {
        return repository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    /**
     * This method may only be accessed by management.
     * @param pageable The pageable to sort and filter the accounts
     * @return A page of all accounts according the pageable object
     */
    @RoleIsManagement
    public Page<Account> getAllAccounts(@NonNull Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Get all accounts under a membership
     * @param memberId The membership ID to compare accounts against
     * @return A sorted/filtered page of accounts
     */
    @PreAuthorize("@accountAuth.memberCanAccess(#memberId)")
    public Page<Account> getAccountsByMemberId(@NonNull long memberId, @NonNull Pageable pageable) {
        return repository.findAllByMemberId(memberId, pageable);
    }

    /**
     * Map account entity to AccountResponse DTO.
     * @param account The account entity to be mapped
     * @return An AccountResponse dto mapped from the passed account entity.
     */
    public AccountResponse mapToResponse(Account account) {
        return mapper.map(account, AccountResponse.class);
    }

    public PaginatedResponse<AccountResponse> mapToPaginatedResponse(Page<Account> accounts) {
        Page<AccountResponse> accountResponses = accounts.map(this::mapToResponse);
        return new PaginatedResponse<>(accountResponses.getContent(),
                                       accounts.getPageable(),
                                       accounts.getTotalElements());
    }

}
