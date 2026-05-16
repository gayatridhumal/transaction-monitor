package com.monitor.transaction.model;

import java.util.List;

public record TransactionResult(
        Transaction transaction,
        List<Alert> alerts
) {
    public boolean flagged() {
        return alerts != null && !alerts.isEmpty();
    }
}