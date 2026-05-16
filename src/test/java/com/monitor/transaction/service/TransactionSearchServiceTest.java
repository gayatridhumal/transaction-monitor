package com.monitor.transaction.service;

import com.monitor.transaction.dto.TransactionSearchRequest;
import com.monitor.transaction.model.Alert;
import com.monitor.transaction.model.AlertType;
import com.monitor.transaction.model.Transaction;
import com.monitor.transaction.model.TransactionResult;
import com.monitor.transaction.repository.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionSearchServiceTest {

    private TransactionSearchService searchService;
    private InMemoryTransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        searchService = new TransactionSearchService(repository);

        Transaction transaction1 = new Transaction(
                "TXN-1001",
                "ACC100",
                "ACC200",
                new BigDecimal("15000"),
                "EUR",
                Instant.parse("2025-07-10T10:00:00Z"),
                "High value transfer"
        );

        Alert alert = new Alert(
                "TXN-1001",
                AlertType.LARGE_AMOUNT,
                "Large transaction",
                Instant.parse("2025-07-10T10:00:01Z")
        );

        repository.save(new TransactionResult(transaction1, List.of(alert)));

        Transaction transaction2 = new Transaction(
                "TXN-1002",
                "ACC300",
                "ACC400",
                new BigDecimal("500"),
                "EUR",
                Instant.parse("2025-07-10T11:00:00Z"),
                "Normal transfer"
        );

        repository.save(new TransactionResult(transaction2, List.of()));
    }

    @Test
    void shouldFilterBySender() {
        TransactionSearchRequest request = new TransactionSearchRequest(
                "ACC100", null, null, null, null, null, null, null
        );

        List<TransactionResult> results = searchService.search(request);

        assertEquals(1, results.size());
        assertEquals("ACC100", results.getFirst().transaction().sender());
    }

    @Test
    void shouldFilterByFlaggedTransactions() {
        TransactionSearchRequest request = new TransactionSearchRequest(
                null, null, null, null, null, null, true, null
        );

        List<TransactionResult> results = searchService.search(request);

        assertEquals(1, results.size());
    }

    @Test
    void shouldFilterByAlertType() {
        TransactionSearchRequest request = new TransactionSearchRequest(
                null, null, null, null, null, null, null, AlertType.LARGE_AMOUNT
        );

        List<TransactionResult> results = searchService.search(request);

        assertEquals(1, results.size());
    }

    @Test
    void shouldReturnAllTransactionsWhenNoFiltersProvided() {
        TransactionSearchRequest request = new TransactionSearchRequest(
                null, null, null, null, null, null, null, null
        );

        List<TransactionResult> results = searchService.search(request);

        assertEquals(2, results.size());
    }
}