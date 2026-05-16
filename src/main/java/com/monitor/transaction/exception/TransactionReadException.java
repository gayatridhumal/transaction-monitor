package com.monitor.transaction.exception;

public class TransactionReadException extends RuntimeException {

    public TransactionReadException(String message, Throwable cause) {
        super(message, cause);
    }
}