package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer (String name, BigDecimal oldBalance, BigDecimal newBalance) {

    public TransactionCustomer {
        Objects.requireNonNull(name);
        Objects.requireNonNull(oldBalance);
        Objects.requireNonNull(newBalance);

        if (oldBalance.signum() < 0) throw new IllegalArgumentException("Old balance must be greater than 0");
        if (newBalance.signum() < 0) throw new IllegalArgumentException("New balance must be greater than 0");
        if (name.trim().isEmpty()) throw new IllegalArgumentException("Name is required");
    }
}
