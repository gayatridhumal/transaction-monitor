package com.monitor.transaction.service;

import com.monitor.transaction.model.Alert;
import com.monitor.transaction.model.Transaction;
import com.monitor.transaction.model.TransactionResult;
import com.monitor.transaction.publisher.AlertPublisher;
import com.monitor.transaction.reader.TransactionReader;
import com.monitor.transaction.repository.TransactionRepository;
import com.monitor.transaction.rule.AlertRule;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TransactionProcessingService {

    private final TransactionReader transactionReader;
    private final List<AlertRule> alertRules;
    private final TransactionRepository transactionRepository;
    private final AlertPublisher alertPublisher;

    public TransactionProcessingService(
            TransactionReader transactionReader,
            List<AlertRule> alertRules,
            TransactionRepository transactionRepository,
            AlertPublisher alertPublisher
    ) {
        this.transactionReader = transactionReader;
        this.alertRules = alertRules;
        this.transactionRepository = transactionRepository;
        this.alertPublisher = alertPublisher;
    }

    public void processAllTransactions() {
        List<Transaction> transactions = transactionReader.readTransactions()
                .stream()
                .sorted(Comparator.comparing(Transaction::timestamp))
                .toList();

        for (Transaction transaction : transactions) {
            List<Alert> alerts = alertRules.stream()
                    .flatMap(rule -> rule.evaluate(transaction).stream())
                    .toList();

            transactionRepository.save(new TransactionResult(transaction, alerts));

            if (!alerts.isEmpty()) {
                alertPublisher.publish(alerts);
            }
        }
    }
}