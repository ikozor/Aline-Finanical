package com.aline.core.model;

import com.aline.core.crypto.AttributeEncryptor;
import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.DateOfBirth;
import com.aline.core.validation.annotation.Name;
import com.aline.core.validation.annotation.PhoneNumber;
import com.aline.core.validation.annotation.SocialSecurity;
import com.aline.core.validation.annotation.Zipcode;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Applicant Model
 * <p>
 *     JPA Entity that represents an applicant.
 * </p>
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Applicant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * First name
     * <p>Uses custom name validator.</p>
     *
     * @see Name
     */
    @Name(message = "'${validatedValue}' is not a valid name.")
    @NotBlank(message = "First name is required.")
    @NotNull
    private String firstName;

    /**
     * Middle name
     * <p><em>Not required.</em></p>
     * <p>Uses custom name validator.</p>
     *
     * @see Name
     */
    @Name(message = "'${validatedValue}' is not a valid name.")
    private String middleName;

    /**
     * Last name
     * <p>Uses custom name validator.</p>
     *
     * @see Name
     */
    @Name(message = "'${validatedValue}' is not a valid name.")
    @NotBlank(message = "Last name is required.")
    @NotNull
    private String lastName;

    /**
     * Date of birth
     * <p>Stored as {@link LocalDate}.</p>
     * <p>Uses custom date of birth validator.</p>
     *
     * @see DateOfBirth
     * @see LocalDate
     */
    @DateOfBirth(minAge = 18, message = "Age must be at least 18.")
    @NotNull(message = "Date of birth is required.")
    private LocalDate dateOfBirth;

    /**
     * Gender
     * <p>Can be one of the following values (case-insensitive):</p>
     * <p><em>Valid values are determined for database uniformity.</em></p>
     * <ul>
     *     <li>Male</li>
     *     <li>Female</li>
     *     <li>Other</li>
     *     <li>Unspecified</li>
     * </ul>
     * @see com.aline.core.model.Gender
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * Email address
     * <p>Must be unique.</p>
     * @see Email
     */
    @Column(unique = true)
    @Email(message = "'${validatedValue}' is not a valid email.")
    @NotBlank(message = "Email is required.")
    @NotNull
    private String email;

    /**
     * Phone number
     * <p>Must be unique.</p>
     * <p>
     *     <em>Ex. (123) 456-7890, +1 321-432-1234</em>
     * </p>
     */
    @Column(unique = true)
    @NotBlank(message = "Phone number is required.")
    @PhoneNumber
    @NotNull
    private String phone;

    /**
     * Social Security number
     * <p>Must be unique.</p>
     * <p>
     *     <em>Ex. 123-45-6789</em>
     * </p>
     */
    @Column(unique = true)
    @NotBlank(message = "Social Security is required.")
    @Convert(converter = AttributeEncryptor.class)
    @SocialSecurity
    @NotNull
    private String socialSecurity;

    /**
     * Drivers license number (can vary per state)
     * <p>Must be unique.</p>
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Driver's license is invalid.")
    @NotNull
    private String driversLicense;

    /**
     * Income (in cents)
     * <p>
     *     <em>Using int for precision.</em>
     * </p>
     * <p>
     *     Income cannot be less than 0.
     * </p>
     */
    @Min(value = 0, message = "You cannot have a negative income.")
    private int income;

    /**
     * Billing address
     * <p>
     *     Must be in street address format.
     * </p>
     * <p>
     *     Uses custom address validator.
     * </p>
     * <p>
     *     <em>Ex. 1234 Address St.</em>
     *     <br>or<br>
     *     <em>1234 Street Ln. Apt. 123</em>
     * </p>
     * @see Address
     */
    @NotBlank(message = "Address is required.")
    @Address(message = "'${validatedValue}' is not a valid address.")
    @NotNull
    private String address;

    /**
     * Billing City
     */
    @NotBlank(message = "City is required.")
    @NotNull
    private String city;

    /**
     * Billing State (USA)
     */
    @NotBlank(message = "State is required.")
    @NotNull
    private String state;

    /**
     * Billing ZIP code
     * <p>Can use 5 digit zip code or ZIP +4 format.</p>
     * <em>
     *     Ex. <code>12345</code> or <code>12345-1234</code>
     * </em>
     */
    @NotBlank(message = "Zipcode is required.")
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @NotNull
    private String zipcode;


    /**
     * Mailing address
     * <p>
     *     Must be in street address format.
     * </p>
     * <p>
     *     Uses custom address validator.
     * </p>
     * <p>
     *     <em>Ex. 1234 Address St.</em>
     *     <br>or<br>
     *     <em>1234 Street Ln. Apt. 123</em>
     * </p>
     * <em>Address Type: MAILING</em>
     * @see Address
     * @see Address.Type
     */
    @NotBlank(message = "Mailing address is required.")
    @Address(message = "'${validatedValue}' is not a valid address.", type = Address.Type.MAILING)
    @NotNull
    private String mailingAddress;

    /**
     * Mailing City
     */
    @NotBlank(message = "Mailing city is required.")
    @NotNull
    private String mailingCity;

    /**
     * Mailing State (USA)
     */
    @NotBlank(message = "Mailing state is required.")
    @NotNull
    private String mailingState;

    /**
     * Mailing ZIP code
     * <p>Can use 5 digit zip code or ZIP +4 format.</p>
     * <em>
     *     Ex. <code>12345</code> or <code>12345-1234</code>
     * </em>
     */
    @NotBlank(message = "Mailing zipcode is required.")
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @NotNull
    private String mailingZipcode;

    /**
     * Applications this applicant has applied under.
     */
    @ManyToMany(mappedBy = "applicants")
    @JsonBackReference
    @ToString.Exclude
    private Set<Application> applications;

    /**
     * Timestamp for the last time this entity was modified.
     */
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;

    /**
     * Timestamp for when the entity was first created.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Applicant applicant = (Applicant) o;
        return Objects.equals(id, applicant.id) && email.equals(applicant.email) && phone.equals(applicant.phone) && socialSecurity.equals(applicant.socialSecurity) && driversLicense.equals(applicant.driversLicense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, socialSecurity, driversLicense);
    }
}
