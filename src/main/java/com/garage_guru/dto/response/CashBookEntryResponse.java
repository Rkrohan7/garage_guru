package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CashBookEntryResponse(
        Long id,
        Long garageId,
        String garageName,
        String transactionType,
        String category,
        BigDecimal amount,
        String description,
        String paymentMethod,
        String referenceNumber,
        String partyName,
        String partyPhone,
        Long invoiceId,
        Long paymentId,
        Long expenseId,
        Long jobCardId,
        LocalDate transactionDate,
        BigDecimal runningBalance,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}