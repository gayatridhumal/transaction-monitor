package com.monitor.transaction.repository;

import com.monitor.transaction.model.Transaction;
import com.monitor.transaction.model.TransactionResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTransactionRepositoryTest {

    @Test
    void shouldSaveAndReturnTransactions() {

        InMemoryTransactionRepository repository =
                new InMemoryTransactionRepository();

        Transaction transaction = new Transaction(
                "TXN-1001",
                "ACC100",
                "ACC200",
                new BigDecimal("1000"),
                "EUR",
                Instant.parse("2025-07-10T10:00:00Z"),
                "Repository test"
        );

        TransactionResult result =
                new TransactionResult(
                        transaction,
                        List.of()
                );

        repository.save(result);

        List<TransactionResult> results = repository.findAll();

        assertEquals(1, results.size());
        assertEquals("TXN-1001",
                results.getFirst().transaction().id());
    }
}