package com.monitor.transaction.config;

import com.monitor.transaction.service.TransactionProcessingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final TransactionProcessingService processingService;

    public StartupRunner(TransactionProcessingService processingService) {
        this.processingService = processingService;
    }

    @Override
    public void run(String... args) {
        processingService.processAllTransactions();
    }
}