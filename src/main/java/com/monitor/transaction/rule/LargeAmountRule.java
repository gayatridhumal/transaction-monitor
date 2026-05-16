package com.monitor.transaction.rule;

import com.monitor.transaction.model.Alert;
import com.monitor.transaction.model.AlertType;
import com.monitor.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Component
public class LargeAmountRule implements AlertRule {

    private static final BigDecimal LIMIT = new BigDecimal("10000");

    @Override
    public List<Alert> evaluate(Transaction transaction) {
        if (transaction.amount().compareTo(LIMIT) > 0) {
            return List.of(new Alert(
                    transaction.id(),
                    AlertType.LARGE_AMOUNT,
                    "Transaction amount exceeds 10000",
                    Instant.now()
            ));
        }

        return List.of();
    }
}