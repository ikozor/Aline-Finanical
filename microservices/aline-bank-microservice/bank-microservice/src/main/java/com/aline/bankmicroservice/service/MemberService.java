package com.aline.bankmicroservice.service;

import com.aline.bankmicroservice.dto.request.MemberSearchCriteria;
import com.aline.bankmicroservice.dto.request.MemberUpdateRequest;
import com.aline.bankmicroservice.dto.response.MemberResponse;
import com.aline.bankmicroservice.specification.MemberSpecification;
import com.aline.core.exception.NotFoundException;
import com.aline.core.exception.notfound.MemberNotFoundException;
import com.aline.core.model.Applicant;
import com.aline.core.model.Member;
import com.aline.core.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("Member Service")
@RequiredArgsConstructor
@Slf4j(topic = "Member Service")
public class MemberService {
    private final MemberRepository memberRepository;
    private final ApplicantService applicantService;
    private final ModelMapper modelMapper;

    /**
     * Searches Database to find members based on Name, Membership ID/Application ID,
     * account status, min/max amounts in accounts, if Members are Primary account holders,
     * if member has checking or savings accounts
     *
     * @param searchCriteria Criteria object that holds the searchable parameters
     * @param pageable       Pageable object
     * @return MemberResponseDTO Paginated response of MemberDTOs
     */
    @PreAuthorize("hasAnyAuthority({'employee', 'administrator'})")
    public Page<MemberResponse> searchMembers(MemberSearchCriteria searchCriteria, Pageable pageable) {
        Specification<Member> specs = Specification.where(MemberSpecification.memberSearch(searchCriteria));
        return memberRepository.findAll(specs, pageable)
                .map(member -> modelMapper.map(member, MemberResponse.class));
    }

    @PreAuthorize("hasAnyAuthority({'employee', 'administrator'})")
    public MemberResponse getMemberByMembershipId(String membershipId){
        return memberRepository.findByMembershipId(membershipId)
                .map(member-> modelMapper.map(member, MemberResponse.class))
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional(rollbackOn = {MemberNotFoundException.class, NotFoundException.class})
    @PreAuthorize("hasAnyAuthority({'employee', 'administrator'})")
    public void updateMember( MemberUpdateRequest update) {
        log.info("Incoming update for Member: {}", update);

        Member member = memberRepository.findByMembershipId(update.getMembershipId()).orElseThrow(MemberNotFoundException::new);

        Applicant applicant = member.getApplicant();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(update, applicant);

        applicantService.saveApplicant(applicant);
        member.setApplicant(applicant);
        memberRepository.save(member);

        log.info("Member was successfully updated to: {}", member);
    }

}
