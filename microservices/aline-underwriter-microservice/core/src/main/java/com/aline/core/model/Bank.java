package com.aline.core.model;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Bank Model
 * <p>
 *     JPA Entity that represents a Bank.
 * </p>
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Routing Number;
     */
    @NotNull
    @Size(min = 9, max = 9)
    private String routingNumber;

    /**
     * Physical Address for Bank
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
    @NotNull
    @NotBlank(message = "Address is required.")
    @Address(message = "'${validatedValue}' is not a valid address.")
    private String address;

    /**
     * City of bank
     */
    @NotNull
    @NotBlank(message = "City is required.")
    private String city;

    /**
     * State of Bank
     */
    @NotNull
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
    @NotBlank(message = "Zipcode is required.")
    @Zipcode(message = "'${validatedValue}' is not in a valid zipcode format.")
    @NonNull
    private String zipcode;

    @OneToMany(mappedBy = "bank")
    private List<Branch> branches;

}
