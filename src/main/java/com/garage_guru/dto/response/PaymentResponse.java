package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Java 21 Record for Payment Response - immutable DTO
 */
public record PaymentResponse(
    Long id,
    Long jobCardId,
    String vehicleNumberPlate,
    BigDecimal amount,
    String paymentMethod,
    String paymentType,
    String description,
    String customerName,
    String customerPhone,
    String vehicleNumber,
    String invoiceNumber,
    LocalDate paymentDate,
    LocalDateTime createdAt,
    Long garageId,
    String garageName
) {}
