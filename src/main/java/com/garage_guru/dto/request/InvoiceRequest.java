package com.garage_guru.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceRequest(
    @NotNull(message = "Garage ID is required")
    Long garageId,

    Long jobCardId,

    @NotBlank(message = "Customer name is required")
    String customerName,

    String customerPhone,

    String customerEmail,

    String customerAddress,

    String vehicleNumber,

    String vehicleMake,

    String vehicleModel,

    @Valid
    List<InvoiceItemRequest> items,

    BigDecimal discountAmount,

    BigDecimal discountPercentage,

    BigDecimal taxAmount,

    BigDecimal taxPercentage,

    LocalDate invoiceDate,

    LocalDate dueDate,

    String notes,

    String termsAndConditions
) {}
