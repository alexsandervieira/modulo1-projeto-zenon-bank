package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.IO.println;
import static java.lang.System.out;

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
        transactions.stream().limit(10).forEach(out::println);

        println("----------------------------------------------------------------------------------------------------");


        List<Transaction> transactionsBadData = ingestor.read("data/paysim_with_bad_data.csv");
        println("Bad Data Size: " + transactionsBadData.size());

        transactionsBadData.forEach(IO::println);


        println("----------------------------------------------------------------------------------------------------");

        var fraudAnalyzer = new FraudAnalyzer(transactions);
        println("Total de Fraudes: " + fraudAnalyzer.countFraud());

        println("As maiores fraudes foram:");
        fraudAnalyzer.findTopFrauds(3).stream().map(Transaction::amount).forEach(IO::println);

        var suspectCustomers = fraudAnalyzer.findTopSuspectCustomers(5);
        println("Top 5 clientes suspeitos");
        suspectCustomers.forEach(IO::println);

        //Calcule o prejuízo total causado pelas fraudes (soma dos amount).
        BigDecimal calculateTotalLoss = fraudAnalyzer.calculateTotalLoss();
        println("Prejuízo total: " + calculateTotalLoss);

        // Conte quantas fraudes ocorreram por tipo de transação (CASH_OUT, TRANSFER, etc...).
        Map<TransactionType, Long> transactionTypeLongMap = fraudAnalyzer.countFraudByType();
        transactionTypeLongMap.forEach((type, count) -> println(type + ": " + count));



        println("----------------------------------------------------------------------------------------------------");

        TransactionRepository repository;

        repository = new TransactionListRepository(transactions);
        //String existsOriginName = "C1231006815";
        String notFound = "C12345";

        String ultimoOriginName = "C1868032458";

        long startTime = System.nanoTime();

                repository
                .findByOriginName(ultimoOriginName)
                .ifPresentOrElse(IO::println, () -> IO.println("Transaction not found " + notFound));

        long endTime = System.nanoTime();

        println("Tempo de execução List - (ms): " + (endTime - startTime) / 1_000_000.0);


        repository = new TransactionMapRepository(transactions);

        long startTimeMap = System.nanoTime();

        repository
                .findByOriginName(ultimoOriginName)
                .ifPresentOrElse(IO::println, () -> out.println("Transaction not found " + notFound));

        long endTimeMap = System.nanoTime();

        println("Tempo de execução Map- (ms): " + (endTimeMap - startTimeMap) / 1_000_000.0);

    }
}

