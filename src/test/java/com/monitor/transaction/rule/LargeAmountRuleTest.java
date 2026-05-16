package com.monitor.transaction.rule;

import com.monitor.transaction.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LargeAmountRuleTest {

    private final LargeAmountRule rule = new LargeAmountRule();

    @Test
    void shouldRaiseAlertWhenAmountExceedsLimit() {

        Transaction transaction = new Transaction(
                "TXN-1",
                "Alice",
                "Bob",
                new BigDecimal("15000"),

                "USD",
                Instant.now(),
                "Payment"
        );

        assertEquals(1, rule.evaluate(transaction).size());
    }

    @Test
    void shouldNotRaiseAlertWhenAmountBelowLimit() {

        Transaction transaction = new Transaction(
                "TXN-2",
                "Alice",
                "Bob",
                new BigDecimal("5000"),
                "USD",
                Instant.now(),
                "Payment"
        );

        assertEquals(0, rule.evaluate(transaction).size());
    }
}