package com.aline.core.model.loan;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float apr;
    private int amount;
    private int term;
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne
    @JoinColumn(name = "deposit_account_id")
    private Account depositAccount;

}
