package com.monitor.transaction.service;

import com.monitor.transaction.dto.TransactionSearchRequest;
import com.monitor.transaction.model.TransactionResult;
import com.monitor.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionSearchService {

    private final TransactionRepository transactionRepository;

    public TransactionSearchService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResult> search(TransactionSearchRequest request) {
        return transactionRepository.findAll()
                .stream()
                .filter(result -> matchesSender(result, request))
                .filter(result -> matchesReceiver(result, request))
                .filter(result -> matchesAmountRange(result, request))
                .filter(result -> matchesDateRange(result, request))
                .filter(result -> matchesFlaggedStatus(result, request))
                .filter(result -> matchesAlertType(result, request))
                .toList();
    }

    private boolean matchesSender(TransactionResult result, TransactionSearchRequest request) {
        return request.sender() == null ||
                result.transaction().sender().equals(request.sender());
    }

    private boolean matchesReceiver(TransactionResult result, TransactionSearchRequest request) {
        return request.receiver() == null ||
                result.transaction().receiver().equals(request.receiver());
    }

    private boolean matchesAmountRange(TransactionResult result, TransactionSearchRequest request) {
        return (request.minAmount() == null ||
                result.transaction().amount().compareTo(request.minAmount()) >= 0)
                &&
                (request.maxAmount() == null ||
                        result.transaction().amount().compareTo(request.maxAmount()) <= 0);
    }

    private boolean matchesDateRange(TransactionResult result, TransactionSearchRequest request) {
        return (request.from() == null ||
                !result.transaction().timestamp().isBefore(request.from()))
                &&
                (request.to() == null ||
                        !result.transaction().timestamp().isAfter(request.to()));
    }

    private boolean matchesFlaggedStatus(TransactionResult result, TransactionSearchRequest request) {
        return request.flagged() == null ||
                result.flagged() == request.flagged();
    }

    private boolean matchesAlertType(TransactionResult result, TransactionSearchRequest request) {
        return request.alertType() == null ||
                result.alerts().stream()
                        .anyMatch(alert -> alert.type() == request.alertType());
    }
}