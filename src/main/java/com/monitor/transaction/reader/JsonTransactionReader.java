package com.monitor.transaction.reader;


import com.monitor.transaction.exception.TransactionReadException;
import com.monitor.transaction.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Component
public class JsonTransactionReader implements TransactionReader {

    private final ObjectMapper objectMapper;
    private final Resource transactionsFile;

    public JsonTransactionReader(
            ObjectMapper objectMapper,
            @Value("classpath:transactions.json") Resource transactionsFile
    ) {
        this.objectMapper = objectMapper;
        this.transactionsFile = transactionsFile;
    }

    @Override
    public List<Transaction> readTransactions() {
        try {
            return objectMapper.readValue(
                    transactionsFile.getInputStream(),
                    new TypeReference<>() {}
            );
        } catch (IOException e) {
            throw new TransactionReadException("Failed to read transactions JSON file", e);        }
    }
}
