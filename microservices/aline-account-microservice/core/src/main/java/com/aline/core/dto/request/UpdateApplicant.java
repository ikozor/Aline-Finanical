package com.aline.core.dto.request;

import com.aline.core.model.Gender;
import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.DateOfBirth;
import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.PhoneNumber;
import com.aline.core.validation.annotation.SocialSecurity;
import com.aline.core.validation.annotation.Zipcode;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO to update an applicant
 * <p>
 *     <em>Similar to {@link CreateApplicant} but fields are nullable.</em>
 * </p>
 * @see CreateApplicant
 */
@Data
@Builder
public class UpdateApplicant implements Serializable {

    /**
     * First name
     * <p>
     *     Validated by {@link Name}
     * </p>
     */
    @Name(message = "'${validatedValue}' is not a valid name.")
    @Nullable
    private String firstName;

    /**
     * Middle name
     * <p>
     *     Validated by {@link Name}
     * </p>
     */
    @Name(message = "'${validatedValue}' is not a valid name.")
    @Nullable
    private String middleName;

    /**
     * Last name
     * <p>
     *     Validated by {@link Name}
     * </p>
     */
    @Name(message = "'${validatedValue}' is not a valid name.")
    @Nullable
    private String lastName;


    /**
     * Date of birth
     * <p>
     *     Validated by {@link DateOfBirth}
     * </p>
     */
    @DateOfBirth(minAge = 18, message = "Age must be at least 18.")
    @Nullable
    private LocalDate dateOfBirth;

    /**
     * Gender
     * <p>Must be one of the values:</p>
     * <ul>
     *     <li>Male</li>
     *     <li>Female</li>
     *     <li>Other</li>
     *     <li>Unspecified</li>
     * </ul>
     * @see Gender
     */
    @Nullable
    private Gender gender;

    /**
     * Email
     * <p>Validated by {@link Email}</p>
     */
    @Email(message = "'${validatedValue}' is not a valid email address.")
    @Nullable
    private String email;

    /**
     * Phone number
     * <p>Validated by {@link PhoneNumber}</p>
     */
    @PhoneNumber
    @Nullable
    private String phone;

    /**
     * Social Security number
     * <p>
     *     Validated by {@link SocialSecurity}
     * </p>
     */
    @SocialSecurity
    @Nullable
    private String socialSecurity;

    /**
     * Driver's license number (can vary per state)
     */
    @Nullable
    private String driversLicense;

    /**
     * Income (in cents)
     * <p>
     *     <em>Using int for precision.</em>
     * </p>
     * <p>Cannot be negative.</p>
     */
    @Min(value = 0, message = "You cannot have a negative income.")
    @Nullable
    private Integer income;

    /**
     * Billing Address
     * <p>Validated by {@link Address}</p>
     */
    @Address(message = "'${validatedValue}' is not a valid address.")
    @Nullable
    private String address;

    /**
     * Billing City
     */
    @Nullable
    private String city;

    /**
     * Billing State
     */
    @Nullable
    private String state;

    /**
     * Billing ZIP code
     * <p>Validated by {@link Zipcode}</p>
     */
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @Nullable
    private String zipcode;

    /**
     * Mailing Address
     * <p>Validated by {@link Address}</p>
     * <em>Address Type: MAILING</em>
     */
    @Address(message = "'${validatedValue}' is not a valid address.", type = Address.Type.MAILING)
    @Nullable
    private String mailingAddress;

    /**
     * Mailing City
     */
    @Nullable
    private String mailingCity;

    /**
     * Mailing State
     */
    @Nullable
    private String mailingState;

    /**
     * Mailing ZIP code
     * <p>Validated by {@link Zipcode}</p>
     */
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @Nullable
    private String mailingZipcode;

}
