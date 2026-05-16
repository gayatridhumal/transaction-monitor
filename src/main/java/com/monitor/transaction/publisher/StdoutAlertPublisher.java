package com.monitor.transaction.publisher;

import com.monitor.transaction.model.Alert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StdoutAlertPublisher implements AlertPublisher {

    @Override
    public void publish(List<Alert> alerts) {
        alerts.forEach(alert ->
                System.out.printf(
                        "ALERT: transactionId=%s, type=%s, message=%s%n",
                        alert.transactionId(),
                        alert.type(),
                        alert.message()
                )
        );
    }
}