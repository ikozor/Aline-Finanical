package com.aline.transactionmicroservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionModelTest {

    Transaction.TransactionBuilder transactionBuilder;

    @BeforeEach
    void setUp() {
        transactionBuilder = Transaction.builder()
                .id(1L)
                .method(TransactionMethod.ACH);
    }

    @Nested
    @DisplayName("Transaction amount > 0")
    class TransactionAmountIsMoreThanZero {

        @Nested
        @DisplayName("Transaction is decreasing")
        class TransactionIsDecreasing {
            @Test
            void typeIsWithdrawal() {
                testTransactionIsDecreasing(TransactionType.WITHDRAWAL);
            }

            @Test
            void typeIsPurchase() {
                testTransactionIsDecreasing(TransactionType.PURCHASE);
            }

            @Test
            void typeIsPayment() {
                testTransactionIsDecreasing(TransactionType.PAYMENT);
            }

            @Test
            void typeIsTransferOut() {
                testTransactionIsDecreasing(TransactionType.TRANSFER_OUT);
            }
        }

        @Nested
        @DisplayName("Transaction is increasing")
        class TransactionIsIncreasing {
            @Test
            void typeIsDeposit() {
                testTransactionIsIncreasing(TransactionType.DEPOSIT);
            }

            @Test
            void typeIsRefund() {
                testTransactionIsIncreasing(TransactionType.REFUND);
            }

            @Test
            void typeIsTransferIn() {
                testTransactionIsIncreasing(TransactionType.TRANSFER_IN);
            }

        }

        @Test
        void transactionIsNonChanging_when_typeIsVoid() {
            testTransactionIsNonChanging(TransactionType.VOID, true);
        }

    }

    @Nested
    @DisplayName("Transaction is non-changing when amount is 0")
    class TransactionIsNonChanging {
        @Test
        void typeIsWithdrawal() {
            testTransactionIsNonChanging(TransactionType.WITHDRAWAL, false);
        }

        @Test
        void typeIsPurchase() {
            testTransactionIsNonChanging(TransactionType.PURCHASE, false);
        }

        @Test
        void typeIsPayment() {
            testTransactionIsNonChanging(TransactionType.PAYMENT, false);
        }

        @Test
        void typeIsTransferOut() {
            testTransactionIsNonChanging(TransactionType.TRANSFER_OUT, false);
        }

        @Test
        void typeIsDeposit() {
            testTransactionIsNonChanging(TransactionType.DEPOSIT, false);
        }

        @Test
        void typeIsRefund() {
            testTransactionIsNonChanging(TransactionType.REFUND, false);
        }

        @Test
        void typeIsTransferIn() {
            testTransactionIsNonChanging(TransactionType.TRANSFER_IN, false);
        }

        @Test
        void typeIsVoid() {
            testTransactionIsNonChanging(TransactionType.VOID, false);
        }

    }

    /**
     * Helper method to test transaction decrease
     * @param type The type of transaction
     *
     */
    private void testTransactionIsDecreasing(TransactionType type) {
        Transaction transaction = transactionBuilder
                .amount(1)
                .type(type)
                .build();
        transaction.checkTransaction();
        assertTrue(transaction.isDecreasing());
        assertFalse(transaction.isIncreasing());
    }

    /**
     * Helper method to test transaction increase
     * @param type The type of transaction
     *
     */
    private void testTransactionIsIncreasing(TransactionType type) {
        Transaction transaction = transactionBuilder
                .amount(1)
                .type(type)
                .build();
        transaction.checkTransaction();
        assertFalse(transaction.isDecreasing());
        assertTrue(transaction.isIncreasing());
    }

    /**
     * Helper method to test transaction increase
     * @param type The type of transaction
     * @param moreThanZero If amount is more than zero
     */
    private void testTransactionIsNonChanging(TransactionType type, boolean moreThanZero) {
        Transaction transaction = transactionBuilder
                .amount(moreThanZero ? 1 : 0)
                .type(type)
                .build();
        transaction.checkTransaction();
        assertFalse(transaction.isDecreasing());
        assertFalse(transaction.isIncreasing());
    }

}
