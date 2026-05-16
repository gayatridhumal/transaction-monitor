# Design Notes

## Overview

The application is a Spring Boot backend service that reads transactions from a JSON file,
evaluates fraud detection rules, stores processed transactions with their alerts in memory,
and exposes a REST API for searching transaction history.

The design keeps transaction input, rule evaluation, alert publishing, and storage separated
so the system remains easy to extend and maintain.

## Main Components

### Transaction Input

`TransactionReader` is an interface responsible for reading transactions.

The current implementation is `JsonTransactionReader`, which loads transactions from
`transactions.json` using Jackson.

This keeps the processing logic independent from the input source. In the future,
transactions could also come from:
- Kafka
- REST ingestion APIs
- Database streams

without changing the fraud detection logic.

---

### Alert Rules

`AlertRule` is the rule interface. Each rule receives a transaction and returns zero or more alerts.

Current rule implementations:

- `LargeAmountRule`
    - Flags transactions where amount is greater than `10000`.

- `HighFrequencyRule`
    - Flags transactions where the same sender performs more than 3 transactions within a rolling 60-second window.

Adding a new rule only requires creating another `AlertRule` implementation.
Existing rules and services do not need to change.

---

### Alert Publishing

`AlertPublisher` is an interface responsible for publishing generated alerts.

The current implementation is `StdoutAlertPublisher`,
which prints alerts to stdout.

This can later be replaced with:
- Kafka publishers
- Database writers
- Notification services
- Monitoring systems

without changing processing logic.

---

### Storage

The repository is intentionally in-memory because the assignment does not require persistence.

`InMemoryTransactionRepository` stores `TransactionResult` objects,
which contain:
- the original transaction
- generated alerts
- flagged status

This keeps the implementation lightweight and easy to test.

## High-Frequency Rule Design

The high-frequency rule is the most performance-sensitive part of the system.

A naive implementation would repeatedly compare transactions from the same sender,
which can become inefficient for larger datasets.

To avoid this, transactions are sorted by timestamp before processing.
`HighFrequencyRule` then uses a sliding window approach implemented with:

```java
Map<String, Deque<Transaction>>
```

For each transaction:

1. Get the sender's current transaction window.
2. Remove transactions older than 60 seconds.
3. Add the current transaction.
4. Raise an alert if the window size exceeds the configured limit.

Each transaction enters and leaves the deque at most once,
which keeps processing efficient.

## Complexity Considerations

Let `n` be the number of transactions.

| Operation | Complexity |
|---|---|
| Read JSON | O(n) |
| Sort transactions | O(n log n) |
| Large amount rule | O(n) |
| High frequency rule | O(n) after sorting |
| Store results | O(n) |
| Search API | O(n) per request |

Overall startup complexity is dominated by sorting.

The search API currently performs linear filtering.
This is a deliberate trade-off to keep the implementation simple and flexible.

If query volume increased, indexes could be added for:
- sender lookups
- receiver lookups
- timestamp range queries
- amount range queries
- alert type filtering

## Exception Handling

Global exception handling is implemented using:

```java
@RestControllerAdvice
```

The application returns structured error responses for:
- invalid request parameters
- conversion failures
- validation failures
- unexpected server errors

## Testing

The project includes:
- Rule tests
- Service tests
- Repository tests
- Controller tests

Both positive and negative scenarios are covered.

The high-frequency rule also includes branch coverage for sliding window behavior.

## Kafka Upgrade Path

The current architecture allows future Kafka integration without changing the rule engine.

A future Kafka consumer could simply pass incoming transactions to the processing service:

```java
@KafkaListener(topics = "transactions")
public void consume(Transaction transaction) {
    transactionProcessingService.process(transaction);
}
```

This keeps ingestion separate from business logic.

## Trade-offs

- In-memory storage was chosen to keep the prototype lightweight.
- Search uses linear filtering instead of indexes for simplicity.
- Transactions are sorted during startup to support efficient sliding window processing.
- Persistence, authentication, pagination, retries, and metrics were intentionally excluded because they are outside the assignment scope.

## Future Improvements

With more time, possible improvements include:
- Kafka ingestion
- Database persistence
- Pagination and sorting support
- Indexed search repository
- Additional fraud detection rules
- Real-time alert publishing
- Metrics and monitoring
- More integration tests