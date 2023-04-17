package com.aline.core.model.credit;

import com.aline.core.model.card.CardIssuer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int creditLimit;
    private float apr;
    private int minPayment;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CreditLineStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CreditLineType creditLineType;

    @NotNull
    private LocalDate startDate;


}
