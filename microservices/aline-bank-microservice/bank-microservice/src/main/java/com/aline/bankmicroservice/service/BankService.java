package com.aline.bankmicroservice.service;

import com.aline.bankmicroservice.dto.request.CreateBank;
import com.aline.bankmicroservice.exception.BankNotFoundException;
import com.aline.core.model.Bank;
import com.aline.core.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Bank Service")
public class BankService {
    private final BankRepository bankRepository;
    private final ModelMapper mapper;

    public Page<Bank> getBanks(Pageable pageable){
        return bankRepository.findAll(pageable);
    }

    public Bank findBankById(Long id){
        long searchId;

        if(id==null || id==0) searchId=1L;
        else searchId = id;
        return bankRepository.findById(searchId).orElseThrow(BankNotFoundException::new);
    }

    @PreAuthorize("hasAnyAuthority({'employee', 'administrator'})")
    public Bank createBank(CreateBank createBank){
        Bank newBank = new Bank();
        mapper.map(createBank, newBank);
        return bankRepository.save(newBank);
    }

    public String getBankRouting(Long id){
        long searchId;

        if(id==null || id==0) searchId=1L;
        else searchId = id;

        Bank bank = bankRepository.findById(searchId).orElseThrow(BankNotFoundException::new);

        return bank.getRoutingNumber();
    }

//    public Bank updateBank()
}
