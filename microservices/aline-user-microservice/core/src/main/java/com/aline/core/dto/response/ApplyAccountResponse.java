package com.aline.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The ApplyAccountResponse class is used in the
 * {@link ApplyResponse} dto to display created
 * account information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyAccountResponse {

    /**
     * Account number that was created.
     */
    private String accountNumber;

    /**
     * Type of the account created.
     */
    private String accountType;

}
