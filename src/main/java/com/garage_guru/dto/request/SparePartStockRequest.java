package com.garage_guru.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Java 21 Record for Spare Part Stock Request - immutable DTO
 */
public record SparePartStockRequest(
    @NotBlank(message = "Item name is required")
    String itemName,

    String manufacturer,

    String partNo,

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    Integer stockQuantity,

    @Min(value = 0, message = "Min stock alert cannot be negative")
    Integer minStockAlert,

    @Min(value = 0, message = "Purchase price cannot be negative")
    BigDecimal purchasePrice,

    @Min(value = 0, message = "Selling price cannot be negative")
    BigDecimal sellingPrice,

    @NotNull(message = "Garage ID is required")
    Long garageId
) {}
