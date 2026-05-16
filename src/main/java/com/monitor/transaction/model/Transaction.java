package com.monitor.transaction.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        String id,
        String sender,
        String receiver,
        BigDecimal amount,
        String currency,
        Instant timestamp,
        String reference
) {
}