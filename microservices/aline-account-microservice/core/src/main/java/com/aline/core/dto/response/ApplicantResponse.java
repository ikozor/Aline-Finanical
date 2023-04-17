package com.aline.core.dto.response;

import com.aline.core.model.Applicant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * Applicant Response DTO
 * <p>
 *     DTO for {@link Applicant} entity.
 *     <br>
 *     <em>One-to-one mapping.</em>
 * </p>
 * @see Applicant
 * @apiNote Use this object as the generic parameter of a ResponseEntity.
 *          This POJO is not being validated.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApplicantResponse {
    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phone;
    private String socialSecurity;
    private String driversLicense;
    private int income;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String mailingAddress;
    private String mailingCity;
    private String mailingState;
    private String mailingZipcode;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime createdAt;

    @JsonIgnore
    private LinkedHashSet<ApplicantResponse> applications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicantResponse applicantResponse = (ApplicantResponse) o;
        return Objects.equals(id, applicantResponse.id) && email.equals(applicantResponse.email) && phone.equals(applicantResponse.phone) && socialSecurity.equals(applicantResponse.socialSecurity) && driversLicense.equals(applicantResponse.driversLicense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, socialSecurity, driversLicense);
    }
}
