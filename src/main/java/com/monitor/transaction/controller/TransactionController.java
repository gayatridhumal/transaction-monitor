package com.monitor.transaction.controller;

import com.monitor.transaction.dto.TransactionSearchRequest;
import com.monitor.transaction.model.AlertType;
import com.monitor.transaction.model.TransactionResult;
import com.monitor.transaction.service.TransactionSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
public class TransactionController {

    private final TransactionSearchService transactionSearchService;

    public TransactionController(TransactionSearchService transactionSearchService) {
        this.transactionSearchService = transactionSearchService;
    }

    @GetMapping("/transactions")
    public List<TransactionResult> searchTransactions(@RequestParam(required = false) String sender,
                                                      @RequestParam(required = false) String receiver,
                                                      @RequestParam(required = false) BigDecimal minAmount,
                                                      @RequestParam(required = false) BigDecimal maxAmount,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                                      @RequestParam(required = false) Boolean flagged,
                                                      @RequestParam(required = false) AlertType alertType) {

        TransactionSearchRequest request = new TransactionSearchRequest(sender, receiver, minAmount, maxAmount, from, to, flagged, alertType);

        return transactionSearchService.search(request);
    }
}