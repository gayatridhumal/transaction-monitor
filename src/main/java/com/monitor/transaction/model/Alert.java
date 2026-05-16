package com.monitor.transaction.model;

import java.time.Instant;

public record Alert(
        String transactionId,
        AlertType type,
        String message,
        Instant raisedAt
) {
}