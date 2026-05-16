package com.monitor.transaction.reader;

import com.monitor.transaction.model.Transaction;

import java.util.List;

public interface TransactionReader {

    List<Transaction> readTransactions();
}