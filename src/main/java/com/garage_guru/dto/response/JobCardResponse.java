package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Java 21 Record for Job Card Response - immutable DTO
 */
public record JobCardResponse(
    Long id,
    String vehicleType,
    String vehicleNumberPlate,
    String make,
    String model,
    String variant,
    Integer kmReading,
    String fuelLevel,
    String jobNotes,
    Boolean flag,
    String status,
    String customerName,
    String customerPhone,
    String customerEmail,
    String customerAddress,
    Long garageId,
    String garageName,

    // Items
    List<JobCardItemResponse> items,
    Integer totalItems,

    // Pricing
    BigDecimal subtotal,
    BigDecimal labourCharges,
    BigDecimal discountAmount,
    BigDecimal taxAmount,
    BigDecimal totalAmount,

    // Timestamps
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime completedAt
) {}