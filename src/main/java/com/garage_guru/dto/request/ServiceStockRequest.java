package com.garage_guru.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Java 21 Record for Service Stock Request - immutable DTO
 */
public record ServiceStockRequest(
    @NotBlank(message = "Service name is required")
    String name,

    @Min(value = 0, message = "Price cannot be negative")
    BigDecimal price,

    @NotNull(message = "Garage ID is required")
    Long garageId
) {}
