package com.aline.transactionmicroservice.dto;

import com.aline.core.dto.request.CardRequest;
import com.aline.core.validation.annotation.AccountNumber;
import com.aline.transactionmicroservice.model.TransactionMethod;
import com.aline.transactionmicroservice.model.TransactionStatus;
import com.aline.transactionmicroservice.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Create a TransactionRequest to make a transaction
 * to a specified account with the specified amount.
 *
 * This DTO is used to manually create a transaction.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransaction {

    /**
     * The type of transaction.
     * Specifies whether it was a purchase, payment,
     * refund, etc...
     */
    @NotNull(message = "Transaction type is required.")
    private TransactionType type;

    /**
     * The method the transaction used whether
     * it was through a credit card or ACH.
     */
    @NotNull(message = "Transaction method is required.")
    private TransactionMethod method;

    @Nullable
    private LocalDateTime date;

    /**
     * The amount of the transaction in cents.
     */
    @PositiveOrZero
    private int amount;

    /**
     * The merchant this transaction was
     * made to (This is only required if the
     * transaction was a purchase, deposit, refund,
     * payment, or void).
     * @see TransactionType
     */
    @Size(min = 4, max = 8)
    private String merchantCode;

    /**
     * A simple merchant name will suffice but can be updated
     * to a full qualifying merchant name. The name does not have
     * to be unique. This is only required if the merchant code
     * specified may not already exist!
     */
    private String merchantName;

    /**
     * Description of the transaction
     */
    @Size(max = 255)
    private String description;

    /**
     * Card request is required if account number
     * is not specified.
     */
    @Valid
    private CardRequest cardRequest;

    /**
     * Account number is required if card number
     * is not specified.
     */
    @AccountNumber
    private String accountNumber;

    /**
     * Specifies if transaction is a pre-authorization transaction
     */
    private boolean hold;
}
