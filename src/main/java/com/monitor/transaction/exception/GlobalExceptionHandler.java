package com.monitor.transaction.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid request parameter");
        problem.setDetail("Invalid value for parameter '%s': %s"
                .formatted(ex.getName(), ex.getValue()));
        problem.setType(URI.create("/errors/invalid-request-parameter"));
        return problem;
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ProblemDetail handleConversionFailed(ConversionFailedException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid value");
        problem.setDetail("Request contains a value that could not be converted to the expected type.");
        problem.setType(URI.create("/errors/conversion-failed"));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");
        problem.setDetail("One or more request fields are invalid.");
        problem.setType(URI.create("/errors/validation-failed"));
        return problem;
    }

    @ExceptionHandler(TransactionReadException.class)
    public ProblemDetail handleTransactionRead(TransactionReadException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Transaction file read error");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("/errors/transaction-read-error"));
        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Application state error");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("/errors/application-state-error"));
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Unexpected error");
        problem.setDetail("An unexpected error occurred.");
        problem.setType(URI.create("/errors/unexpected-error"));
        return problem;
    }
}