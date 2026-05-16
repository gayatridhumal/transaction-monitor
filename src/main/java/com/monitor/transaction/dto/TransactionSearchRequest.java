package com.monitor.transaction.dto;

import com.monitor.transaction.model.AlertType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionSearchRequest(

        String sender,

        String receiver,

        BigDecimal minAmount,

        BigDecimal maxAmount,

        Instant from,

        Instant to,

        Boolean flagged,

        AlertType alertType
) {
}