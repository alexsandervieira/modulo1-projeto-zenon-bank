package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(int step,
                          TransactionType type, BigDecimal amount, TransactionCustomer origin,
                          TransactionCustomer recipient, boolean isFraud, boolean isFlaggedFraud) {

    public Transaction {
        Objects.requireNonNull(step);
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(recipient);

        if (step < 1) throw new IllegalArgumentException("Step must be greater than 0");
        if (amount.signum() < 0) throw new IllegalArgumentException("Amount must be greater than 0");
    }

}
