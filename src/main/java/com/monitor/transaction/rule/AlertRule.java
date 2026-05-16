package com.monitor.transaction.rule;

import com.monitor.transaction.model.Alert;
import com.monitor.transaction.model.Transaction;

import java.util.List;

public interface AlertRule {

    List<Alert> evaluate(Transaction transaction);
}