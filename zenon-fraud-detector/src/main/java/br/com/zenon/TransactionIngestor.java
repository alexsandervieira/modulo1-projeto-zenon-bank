package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TransactionIngestor {

    public List<Transaction> read(String filename) {

        Path path = Paths.get(filename);
        try {
            Files.readAllLines(path);

            return  Files.readAllLines(path).stream().skip(1)
                    .limit(1000)
                    .map(this::getTransaction)
                    .toList();


        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo " + filename, e);
        }

    }

    private Transaction getTransaction(String line) {
        String[] chunks = line.split(",");

        var step = Integer.parseInt(chunks[0]);
        var type = TransactionType.valueOf(chunks[1]);
        var amount = new BigDecimal(chunks[2]);
        var origin = new TransactionCustomer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
        var recipient = new TransactionCustomer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));
        var isFraud = "1".equals(chunks[9]);
        var isFlaggedFraud = "1".equals(chunks[10]);

        var transaction = new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud);
        return transaction;
    }
}
