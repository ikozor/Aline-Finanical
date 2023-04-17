package com.aline.core.model.credit;

import com.aline.core.model.card.CardIssuer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer amount;

    @NotNull
    private Float minApr;

    @NotNull
    private Float maxApr;

    @NotNull
    private Integer minPayment;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String offerName;

    @NotNull
    @NotBlank
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "card_issuer_name")
    private CardIssuer cardIssuer;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CreditLineType creditLineType;

}
