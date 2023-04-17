package com.aline.core.model.account;

import com.aline.core.model.credit.CreditLine;
import com.aline.core.model.payment.Payment;
import com.aline.core.model.payment.PaymentRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue(AccountType.Values.CREDIT_CARD)
public class CreditCardAccount extends Account {
    private int availableCredit;
    @OneToOne
    private CreditLine creditLine;
    @OneToMany
    @JoinTable(name = "cc_account_payments")
    private List<Payment> payments;
    @OneToMany
    @JoinTable(name = "cc_account_payment_history")
    private List<PaymentRecord> paymentHistory;

    @Transient
    public void decreaseAvailableCredit(int amount) {
        availableCredit -= amount;
    }

    @Transient
    public void increaseAvailableCredit(int amount) {
        availableCredit += amount;
    }

    // Reversed functionality for credit cards
    // Balance is how much is owed instead of how much is available
    @Override
    public void increaseBalance(int amount) {
        setBalance(getBalance() - amount);
    }

    // Reversed functionality for credit cards
    // Balance is how much is owed instead of how much is available
    @Override
    public void decreaseBalance(int amount) {
        setBalance(getBalance() + amount);
    }
}
