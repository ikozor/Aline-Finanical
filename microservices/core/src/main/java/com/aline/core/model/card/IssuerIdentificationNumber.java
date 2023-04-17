package com.aline.core.model.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IssuerIdentificationNumber {
    @Id
    @Length(max = 4)
    private String iin;

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_issuer_name")
    @NotNull
    private CardIssuer cardIssuer;
}
