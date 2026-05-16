package com.monitor.transaction.repository;

import com.monitor.transaction.model.TransactionResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<TransactionResult> results = new ArrayList<>();

    @Override
    public void save(TransactionResult result) {
        results.add(result);
    }

    @Override
    public List<TransactionResult> findAll() {
        return List.copyOf(results);
    }
}