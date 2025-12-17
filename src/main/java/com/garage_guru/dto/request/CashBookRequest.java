package com.garage_guru.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CashBookRequest(
        @NotNull(message = "Garage ID is required")
        Long garageId,

        @NotBlank(message = "Transaction type is required")
        String transactionType,  // CREDIT or DEBIT

        @NotBlank(message = "Category is required")
        String category,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "Description is required")
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

        String notes
) {}