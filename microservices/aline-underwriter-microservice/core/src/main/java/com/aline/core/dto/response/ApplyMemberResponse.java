package com.aline.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ApplyMemberResponse class is used
 * in the {@link ApplyResponse} dto to
 * display created member information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyMemberResponse {

    /**
     * Member ID that was created.
     */
    private String membershipId;

    /**
     * Name of the created member
     */
    private String name;

}
