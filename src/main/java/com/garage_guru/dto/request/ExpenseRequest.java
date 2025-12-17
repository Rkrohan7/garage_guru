package com.garage_guru.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Java 21 Record for Expense Request - immutable DTO
 */
public record ExpenseRequest(
    @NotBlank(message = "Title is required")
    String title,

    String description,

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,

    @NotBlank(message = "Category is required")
    String category,  // RENT, SALARY, UTILITIES, MAINTENANCE, INVENTORY, FUEL, TOOLS, MARKETING, INSURANCE, TAX, OTHER

    String paymentMethod,  // CASH, CARD, UPI, BANK_TRANSFER

    String vendorName,

    String receiptNumber,

    LocalDate expenseDate,

    @NotNull(message = "Garage ID is required")
    Long garageId
) {}
