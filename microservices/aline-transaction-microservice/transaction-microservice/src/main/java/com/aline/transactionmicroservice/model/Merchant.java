package com.aline.transactionmicroservice.model;

import com.aline.core.validation.annotation.Address;
import com.aline.core.validation.annotation.Zipcode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A merchant is an entity that receives or
 * awards funds from or to a member's account
 * either through direct deposit, ACH, or checks.
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

    /**
     * The merchant code used to identify a merchant
     */
    @Id
    @Length(min = 5, max = 8)
    private String code;

    /**
     * Full qualified name of a merchant
     */
    @NotNull
    @Length(max = 150)
    private String name;

    /**
     * Optional description of the merchant.
     */
    @Length(max = 255)
    private String description;

    /**
     * Merchant's address
     */
    @Address
    private String address;

    private String city;

    private String state;

    @Zipcode
    private String zipcode;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Merchant merchant = (Merchant) o;
        return code.equals(merchant.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
