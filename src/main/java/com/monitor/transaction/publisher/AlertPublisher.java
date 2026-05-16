package com.monitor.transaction.publisher;

import com.monitor.transaction.model.Alert;

import java.util.List;

public interface AlertPublisher {

    void publish(List<Alert> alerts);
}