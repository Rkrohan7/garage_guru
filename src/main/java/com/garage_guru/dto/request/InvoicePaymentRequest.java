package com.garage_guru.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoicePaymentRequest(
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    BigDecimal amount,

    @NotBlank(message = "Payment method is required")
    String paymentMethod,  // CASH, CARD, UPI, BANK_TRANSFER

    LocalDate paymentDate,

    String description
) {}