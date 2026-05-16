package com.monitor.transaction.controller;

import com.monitor.transaction.model.Alert;
import com.monitor.transaction.model.AlertType;
import com.monitor.transaction.model.Transaction;
import com.monitor.transaction.model.TransactionResult;
import com.monitor.transaction.service.TransactionSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionSearchService searchService;

    @Test
    void shouldReturnTransactions() throws Exception {
        Transaction transaction = new Transaction(
                "TXN-1001",
                "ACC100",
                "ACC200",
                new BigDecimal("15000"),
                "EUR",
                Instant.parse("2025-07-10T10:00:00Z"),
                "High value transfer"
        );

        Alert alert = new Alert(
                "TXN-1001",
                AlertType.LARGE_AMOUNT,
                "Large transaction",
                Instant.parse("2025-07-10T10:00:01Z")
        );

        when(searchService.search(any()))
                .thenReturn(List.of(new TransactionResult(transaction, List.of(alert))));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transaction.id").value("TXN-1001"))
                .andExpect(jsonPath("$[0].transaction.currency").value("EUR"))
                .andExpect(jsonPath("$[0].alerts[0].type").value("LARGE_AMOUNT"));
    }

    @Test
    void shouldAcceptSearchParameters() throws Exception {
        when(searchService.search(any())).thenReturn(List.of());

        mockMvc.perform(get("/transactions")
                        .param("sender", "ACC100")
                        .param("receiver", "ACC200")
                        .param("minAmount", "1000")
                        .param("maxAmount", "20000")
                        .param("from", "2025-07-10T10:00:00Z")
                        .param("to", "2025-07-10T12:00:00Z")
                        .param("flagged", "true")
                        .param("alertType", "LARGE_AMOUNT"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForInvalidAlertType() throws Exception {
        mockMvc.perform(get("/transactions")
                        .param("alertType", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request parameter"));
    }
}