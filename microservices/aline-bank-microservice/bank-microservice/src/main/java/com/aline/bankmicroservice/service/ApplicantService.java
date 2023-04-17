package com.aline.bankmicroservice.service;

import com.aline.core.model.Applicant;
import com.aline.core.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public void saveApplicant(Applicant applicant) {applicantRepository.save(applicant);}
}
