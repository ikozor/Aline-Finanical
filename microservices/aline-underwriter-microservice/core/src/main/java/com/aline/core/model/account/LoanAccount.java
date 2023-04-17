package com.aline.core.model.account;

import com.aline.core.model.loan.Loan;
import com.aline.core.model.payment.Payment;
import com.aline.core.model.payment.PaymentRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(AccountType.Values.LOAN)
public class LoanAccount extends Account {
    @OneToOne
    private Loan loan;
    @OneToMany
    @JoinTable(name = "loan_account_payments")
    private List<Payment> payments;
    @OneToMany
    @JoinTable(name = "loan_account_payment_history")
    private List<PaymentRecord> paymentHistory;
}
