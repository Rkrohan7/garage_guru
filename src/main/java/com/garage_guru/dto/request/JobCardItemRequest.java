package com.garage_guru.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record JobCardItemRequest(
    @NotBlank(message = "Item type is required")
    String itemType,  // SPARE_PART, LUBE, SERVICE, LABOUR, OTHER

    @NotBlank(message = "Item name is required")
    String itemName,

    String itemCode,

    String description,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    Integer quantity,

    @NotNull(message = "Unit price is required")
    BigDecimal unitPrice,

    BigDecimal discountAmount,

    BigDecimal taxPercentage,

    BigDecimal taxAmount,

    Long sparePartStockId,

    Long lubesStockId,

    Long serviceStockId
) {}