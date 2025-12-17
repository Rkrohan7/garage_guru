package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record JobCardItemResponse(
    Long id,
    String itemType,
    String itemName,
    String itemCode,
    String description,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal discountAmount,
    BigDecimal taxPercentage,
    BigDecimal taxAmount,
    BigDecimal totalPrice,
    Long sparePartStockId,
    Long lubesStockId,
    Long serviceStockId,
    LocalDateTime createdAt
) {}