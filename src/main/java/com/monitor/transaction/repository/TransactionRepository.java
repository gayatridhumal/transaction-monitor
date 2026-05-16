package com.monitor.transaction.repository;

import com.monitor.transaction.model.TransactionResult;

import java.util.List;

public interface TransactionRepository {

    void save(TransactionResult result);

    List<TransactionResult> findAll();
}