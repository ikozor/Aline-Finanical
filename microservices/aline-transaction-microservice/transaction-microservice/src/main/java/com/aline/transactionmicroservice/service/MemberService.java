package com.aline.transactionmicroservice.service;

import com.aline.core.exception.notfound.MemberNotFoundException;
import com.aline.core.model.Member;
import com.aline.core.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    /**
     * Get member by ID
     * @param id The ID of the member
     * @return A member with the specified ID
     */
    public Member getMemberById(long id) {
        return repository.findById(id).orElseThrow(MemberNotFoundException::new);
    }
}
