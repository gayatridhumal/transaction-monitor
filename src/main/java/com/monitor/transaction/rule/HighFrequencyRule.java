package com.monitor.transaction.rule;

import com.monitor.transaction.model.Alert;
import com.monitor.transaction.model.AlertType;
import com.monitor.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HighFrequencyRule implements AlertRule {

    private static final int MAX_TRANSACTIONS = 3;
    private static final long WINDOW_SECONDS = 60;

    private final Map<String, Deque<Transaction>> recentTransactionsBySender =
            new HashMap<>();

    @Override
    public List<Alert> evaluate(Transaction transaction) {

        Deque<Transaction> window =
                recentTransactionsBySender.computeIfAbsent(
                        transaction.sender(),
                        key -> new ArrayDeque<>()
                );

        Instant cutoff =
                transaction.timestamp().minusSeconds(WINDOW_SECONDS);

        while (!window.isEmpty()
                && window.peekFirst().timestamp().isBefore(cutoff)) {

            window.removeFirst();
        }

        window.addLast(transaction);

        if (window.size() > MAX_TRANSACTIONS) {

            return List.of(new Alert(
                    transaction.id(),
                    AlertType.HIGH_FREQUENCY,
                    "More than 3 transactions within 60 seconds",
                    Instant.now()
            ));
        }

        return List.of();
    }
}