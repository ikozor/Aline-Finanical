package com.aline.bankmicroservice.dto.response;

import com.aline.core.model.Applicant;
import com.aline.core.model.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MemberResponse {

    private Long id;

    private String membershipId;

    private Applicant applicant;

    private Branch branch;
}
