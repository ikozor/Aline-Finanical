package com.aline.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApplicationResponse {

    private long id;
    private String applicationType;
    private String applicationStatus;
    private ApplicantResponse primaryApplicant;
    private Set<ApplicantResponse> applicants;
}
