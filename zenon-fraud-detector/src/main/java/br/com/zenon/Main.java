package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.IO.println;

public class Main {

    void main() {
        var t1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                        false, false);


        var t2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true, false);

        println(t1);
        println(t2);

        println("----------------------------------------------------------------------------------------------------");

        var ingestor = new TransactionIngestor();
        List<Transaction> transactions =  ingestor.read("data/PS_20174392719_1491204439457_log.csv");

        println(transactions.size());
        transactions.stream().limit(10).forEach(System.out::println);


    }
}

