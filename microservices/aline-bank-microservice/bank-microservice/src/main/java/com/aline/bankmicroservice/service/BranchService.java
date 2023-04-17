package com.aline.bankmicroservice.service;

import com.aline.bankmicroservice.dto.request.CreateBranch;
import com.aline.bankmicroservice.dto.request.UpdateBranch;
import com.aline.bankmicroservice.exception.BankNotFoundException;
import com.aline.core.exception.notfound.BranchNotFoundException;
import com.aline.core.model.Bank;
import com.aline.core.model.Branch;
import com.aline.core.paginated.BranchPaginated;
import com.aline.core.repository.BankRepository;
import com.aline.core.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service("Branch Service")
@RequiredArgsConstructor
@Slf4j(topic = "Branch Service")
public class BranchService {
    private final BranchRepository branchRepository;
    private final BankRepository bankRepository;
    private final ModelMapper mapper;

    public BranchPaginated getBranches(Pageable pageable) {
        Page<Branch> branchPage = branchRepository.findAll(pageable);
        return new BranchPaginated(branchPage.getContent(), pageable, branchPage.getTotalElements());
    }

    /**
     * Creates a branch
     * @param branchInfo - a branch dto with bank ID to link the branch to.
     */
    @PreAuthorize("hasAnyAuthority({'employee', 'administrator'})")
    public Branch postBranch(CreateBranch branchInfo){
        Bank bank = bankRepository.findById(branchInfo.getBankID()).orElseThrow(BankNotFoundException::new);

        Branch branch = new Branch();
        mapper.map(branchInfo, branch);
        branch.setBank(bank);
        return branchRepository.save(branch);
    }

    /**
     * Update a branch using branch ID
     * @param branchInfo
     */
    @PreAuthorize("hasAnyAuthority({'employee', 'administrator'})")
    public void updateBranch(UpdateBranch branchInfo){

        Branch branch = branchRepository.findById(branchInfo.getId()).orElseThrow(BranchNotFoundException::new);

        ModelMapper myMapper = new ModelMapper();
        myMapper.getConfiguration().setSkipNullEnabled(true);
        myMapper.map(branchInfo, branch);

        if(branchInfo.getBankID()!=null){
            Bank bank = bankRepository.findById(branchInfo.getBankID()).orElseThrow(BankNotFoundException::new);
            branch.setBank(bank);
        }

        branchRepository.save(branch);
    }

    /**
     * Delete Branch By Id
     * @param branchId
     */
    @PreAuthorize("hasAnyAuthority({'administrator', 'employee'})")
    public void deleteBranch(Long branchId){
        branchRepository.deleteById(branchId);
    }

}
