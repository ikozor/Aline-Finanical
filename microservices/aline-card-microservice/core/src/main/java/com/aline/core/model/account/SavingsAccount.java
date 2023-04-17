package com.aline.core.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(AccountType.Values.SAVINGS)
public class SavingsAccount extends Account {

    /**
     * Annual Percentage Yield
     * <p>
     *     This is the amount of interest this
     *     account will earn per year.
     * </p>
     */
    private float apy;
}
