package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Java 21 Record for Expense Response - immutable DTO
 */
public record ExpenseResponse(
    Long id,
    String title,
    String description,
    BigDecimal amount,
    String category,
    String paymentMethod,
    String vendorName,
    String receiptNumber,
    LocalDate expenseDate,
    LocalDateTime createdAt,
    Long garageId,
    String garageName
) {}