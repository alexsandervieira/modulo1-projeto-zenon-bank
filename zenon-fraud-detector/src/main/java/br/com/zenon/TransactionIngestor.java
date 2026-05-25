package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    private static final int LIMIT = 50_000;

    public List<Transaction> read(String filename) {

        Path path = Paths.get(filename);
        try {
            Files.readAllLines(path);

            return Files.readAllLines(path).stream()
                    .skip(1)
                    .limit(LIMIT)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo " + filename, e);
        }

    }

    private Optional<Transaction> parseTransaction(String line) {

        try {
            String[] chunks = line.split(",");

            var step = Integer.parseInt(chunks[0]);
            var type = TransactionType.valueOf(chunks[1]);


            if (chunks[2] == null || chunks[2].trim().isEmpty()) {
                throw new IllegalArgumentException("Amount is required, not null or empty");
            }
            var amount = new BigDecimal(chunks[2]);

            var origin = new TransactionCustomer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
            var recipient = new TransactionCustomer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));

            var isFraud = "1".equals(chunks[9]);
            var isFlaggedFraud = "1".equals(chunks[10]);

            return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));

        } catch (Exception e) {
            IO.println("Erro ao fazer o parse da linha " + line + " | " + e);
        }

        return Optional.empty();

    }
}
