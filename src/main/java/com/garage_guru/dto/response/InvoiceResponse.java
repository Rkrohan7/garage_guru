package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponse(
    Long id,
    String invoiceNumber,
    Long jobCardId,
    Long garageId,
    String garageName,

    // Customer Details
    String customerName,
    String customerPhone,
    String customerEmail,
    String customerAddress,

    // Vehicle Details
    String vehicleNumber,
    String vehicleMake,
    String vehicleModel,

    // Invoice Items
    List<InvoiceItemResponse> items,
    Integer totalItems,

    // Pricing
    BigDecimal subtotal,
    BigDecimal discountAmount,
    BigDecimal discountPercentage,
    BigDecimal taxAmount,
    BigDecimal taxPercentage,
    BigDecimal totalAmount,
    BigDecimal paidAmount,
    BigDecimal dueAmount,

    // Status
    String status,
    String paymentStatus,

    // Dates
    LocalDate invoiceDate,
    LocalDate dueDate,
    LocalDate paidDate,

    // Notes
    String notes,
    String termsAndConditions,

    // Audit
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}