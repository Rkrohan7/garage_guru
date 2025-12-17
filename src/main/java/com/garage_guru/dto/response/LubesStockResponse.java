package com.garage_guru.dto.response;

import java.math.BigDecimal;

/**
 * Java 21 Record for Lubes Stock Response - immutable DTO
 */
public record LubesStockResponse(
    Long id,
    String itemName,
    String manufacturer,
    String partNo,
    Integer stockQuantity,
    Integer minStockAlert,
    BigDecimal purchasePrice,
    BigDecimal sellingPrice,
    Long garageId,
    String garageName,
    Boolean lowStock
) {}
