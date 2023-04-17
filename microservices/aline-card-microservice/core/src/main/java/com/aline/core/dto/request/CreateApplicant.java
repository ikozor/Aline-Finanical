package com.aline.core.dto.request;

import com.aline.core.model.Gender;
import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.DateOfBirth;
import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.PhoneNumber;
import com.aline.core.validation.annotation.SocialSecurity;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO to create an applicant
 * <p>Uses custom validators:</p>
 * <ul>
 *     <li>{@link Name}</li>
 *     <li>{@link DateOfBirth}</li>
 *     <li>{@link Gender}</li>
 *     <li>{@link PhoneNumber}</li>
 *     <li>{@link SocialSecurity}</li>
 *     <li>{@link Address}</li>
 *     <li>{@link Zipcode}</li>
 * </ul>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateApplicant implements Serializable {

    /**
     * First name
     * <p>
     *     Validated by {@link Name}
     * </p>
     */
    @Name(message = "'${validatedValue}' is not a valid name.")
    @NotBlank(message = "First name is required.")
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
    @NotBlank(message = "Last name is required.")
    private String lastName;


    /**
     * Date of birth
     * <p>
     *     Validated by {@link DateOfBirth}
     * </p>
     */
    @DateOfBirth(minAge = 18, message = "Age must be at least 18.")
    @NotNull(message = "Date of birth is required.")
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
     *
     * @see Gender
     */
    @NotNull(message = "Gender is required.")
    private Gender gender;

    /**
     * Email
     * <p>Validated by {@link Email}</p>
     */
    @Email(message = "'${validatedValue}' is not a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;

    /**
     * Phone number
     * <p>Validated by {@link PhoneNumber}</p>
     */
    @PhoneNumber
    @NotBlank(message = "Phone number is required.")
    private String phone;

    /**
     * Social Security number
     * <p>
     *     Validated by {@link SocialSecurity}
     * </p>
     */
    @SocialSecurity
    @NotBlank(message = "Social Security number is required.")
    private String socialSecurity;

    /**
     * Driver's license number (can vary per state)
     */
    @NotBlank(message = "Driver's license is required.")
    private String driversLicense;

    /**
     * Income (in cents)
     * <p>
     *     <em>Using int for precision.</em>
     * </p>
     * <p>Cannot be negative.</p>
     */
    @Min(value = 0, message = "You cannot have a negative income.")
    @NotNull(message = "Income is required")
    private Integer income;

    /**
     * Billing Address
     * <p>Validated by {@link Address}</p>
     */
    @Address(message = "'${validatedValue}' is not a valid address.")
    @NotBlank(message = "Address is required.")
    private String address;

    /**
     * Billing City
     */
    @NotBlank(message = "City is required.")
    private String city;

    /**
     * Billing State
     */
    @NotBlank(message = "State is required.")
    private String state;

    /**
     * Billing ZIP code
     * <p>Validated by {@link Zipcode}</p>
     */
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @NotBlank(message = "Zipcode is required.")
    private String zipcode;

    /**
     * Mailing Address
     * <p>Validated by {@link Address}</p>
     * <em>Address Type: MAILING</em>
     */
    @Address(message = "'${validatedValue}' is not a valid address.", type = Address.Type.MAILING)
    @NotBlank(message = "Mailing address is required.")
    private String mailingAddress;

    /**
     * Mailing City
     */
    @NotBlank(message = "Mailing city is required.")
    private String mailingCity;

    /**
     * Mailing State
     */
    @NotBlank(message = "Mailing state is required.")
    private String mailingState;

    /**
     * Mailing ZIP code
     * <p>Validated by {@link Zipcode}</p>
     */
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @NotBlank(message = "Mailing zipcode is required.")
    private String mailingZipcode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateApplicant that = (CreateApplicant) o;
        return email.equals(that.email) && phone.equals(that.phone) && socialSecurity.equals(that.socialSecurity) && driversLicense.equals(that.driversLicense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, phone, socialSecurity, driversLicense);
    }
}
