package com.garage_guru.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Java 21 Record for Payment Request - immutable DTO
 */
public record PaymentRequest(
    Long jobCardId,

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,

    @NotBlank(message = "Payment method is required")
    String paymentMethod,  // CASH, CARD, UPI, BANK_TRANSFER

    @NotBlank(message = "Payment type is required")
    String paymentType,  // SERVICE, SPARE_PART, LUBE, OTHER

    String description,

    String customerName,

    String customerPhone,

    String vehicleNumber,

    String invoiceNumber,

    LocalDate paymentDate,

    @NotNull(message = "Garage ID is required")
    Long garageId
) {}
