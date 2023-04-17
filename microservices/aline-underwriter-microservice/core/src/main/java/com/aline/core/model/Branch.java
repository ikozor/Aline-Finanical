package com.aline.core.model;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Branch Model
 * <p>
 *     JPA Entity that represents a Branch
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Branch Name
     */
    @NotNull
    private String name;

    /**
     * Physical Address for Branch
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
     * City
     */
    @NotBlank(message = "City is required")
    @NotNull
    private String city;

    /**
     * State
     */
    @NotBlank(message = "State is required.")
    private String state;

    /**
     * Zipcode
     * <p>Can use 5 digit zip code or ZIP +4 format.</p>
     * <em>
     *     Ex. <code>12345</code> or <code>12345-1234</code>
     * </em>
     * @see Zipcode
     */
    @NotBlank(message = "Zipcode is required")
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @NonNull
    private String zipcode;

    /**
     * Bank phone number
     */
    @Column(unique = true)
    @NotBlank(message = "Phone number is required")
    @NonNull
    private String phone;

    /**
     * Associated Bank
     */
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

}
