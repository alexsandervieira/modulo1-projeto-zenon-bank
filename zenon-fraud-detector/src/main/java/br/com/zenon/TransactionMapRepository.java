package br.com.zenon;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactions;


    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions.stream()
                .collect(Collectors.toMap(transaction -> transaction.origin().name(), Function.identity()));
    }

    public Optional<Transaction> findByOriginName(String OriginName) {
        return Optional.ofNullable(transactions.get(OriginName));
    }
}
