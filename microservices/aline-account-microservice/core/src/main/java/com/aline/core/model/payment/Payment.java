package com.aline.core.model.payment;

import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
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
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Min(0)
    private int amount;
    private String description;
    @NotNull
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus status;
    @ManyToOne
    @NotNull
    private Member payer;
    @ManyToOne
    @NotNull
    private Account payToAccount;

}
