package com.aline.transactionmicroservice.model;

import com.aline.core.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transactions affect the balance of the associated
 * account.
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    /**
     * Transaction ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The transaction method.
     * <br>
     * <em>How the transaction was processed</em>
     */
    @NotNull(message = "Transaction method is required.")
    @Enumerated(EnumType.STRING)
    private TransactionMethod method;

    /**
     * Transaction amount
     * <br>
     * <em>Represented in cents. (Integer instead of float)</em>
     */
    @NotNull(message = "Transaction amount is required.")
    @PositiveOrZero
    private Integer amount;

    /**
     * The account the transaction is being
     * applied to. (This is very important to include
     * in the DTO)
     */
    @NotNull(message = "An account is required.")
    @ManyToOne(optional = false)
    private Account account;

    /**
     * The balance of the account at the time
     * of the transaction <em>(before anything is applied
     * to the account)</em>
     */
    @NotNull(message = "The account initial balance is required.")
    private Integer initialBalance;

    /**
     * The balance actually posted to the account after
     * the transaction is applied
     */
    private Integer postedBalance;

    /**
     * Transaction type specifies whether it
     * was a purchase, payment, refund, etc...
     */
    @NotNull(message = "Transaction type is required.")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * Transaction status (ACTIVE, PENDING, DENIED)
     */
    @NotNull(message = "Transaction status is required.")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    /**
     * The state the transaction is currently in
     */
    @NotNull(message = "Transaction state is required.")
    @Enumerated(EnumType.STRING)
    private TransactionState state;

    /**
     * Description of the transaction
     */
    @Length(max = 255)
    private String description;

    /**
     * Non-option merchant
     */
    @ManyToOne
    private Merchant merchant;

    @NotNull
    private LocalDateTime date;

    /**
     * The date the transaction was made
     */
    @CreationTimestamp
    private LocalDateTime created;

    /**
     * The last time the transaction was modified
     */
    @UpdateTimestamp
    private LocalDateTime lastModified;

    /**
     * True if the transaction increases the account balance
     */
    @Transient
    private boolean increasing;

    /**
     * True if the transaction decreases the account balance
     */
    @Transient
    private boolean decreasing;

    /**
     * True if the transaction type is a merchant transaction type
     */
    @Transient
    private boolean merchantTransaction;

    @PostLoad
    @PrePersist
    @PostPersist
    @PreUpdate
    @PostUpdate
    public void checkTransaction() {

        // Check if transaction was requested by a merchant
        checkTransactionType();

        // Set the boolean values: increasing and decreasing
        checkIncreaseDecrease();

    }

    private void checkTransactionType() {
        switch (type) {
            case PURCHASE:
            case PAYMENT:
            case REFUND:
            case VOID:
            case DEPOSIT:
                merchantTransaction = true;
                break;
            default:
                merchantTransaction = false;
                break;
        }
    }

    private void checkIncreaseDecrease() {
        // If amount is <= 0, then it will neither be increase nor decrease
        if (amount <= 0) {
            increasing = false;
            decreasing = false;
            return;
        }

        // Set increase decrease based on transaction type
        switch (type) {
            case WITHDRAWAL:
            case PURCHASE:
            case PAYMENT:
            case TRANSFER_OUT:
                increasing = false;
                decreasing = true;
                break;
            case DEPOSIT:
            case REFUND:
            case TRANSFER_IN:
                increasing = true;
                decreasing = false;
                break;
            // Regardless of the amount, if the transaction is void,
            // then it will not increase or decrease
            case VOID:
                increasing = false;
                decreasing = false;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) && amount.equals(that.amount) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date);
    }
}
