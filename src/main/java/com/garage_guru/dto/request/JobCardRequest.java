package com.garage_guru.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Java 21 Record for Job Card Request - immutable DTO
 */
public record JobCardRequest(
    String vehicleType,

    @NotBlank(message = "Vehicle number plate is required")
    String vehicleNumberPlate,

    String make,

    String model,

    String variant,

    Integer kmReading,

    String fuelLevel,

    String jobNotes,

    Boolean flag,

    String status,  // PENDING, IN_PROGRESS, COMPLETED

    String customerName,

    String customerPhone,

    String customerEmail,

    String customerAddress,

    BigDecimal labourCharges,

    BigDecimal discountAmount,

    BigDecimal taxAmount,

    @NotNull(message = "Garage ID is required")
    Long garageId
) {}
