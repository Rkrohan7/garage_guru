package com.garage_guru.dto.response;

import java.math.BigDecimal;

/**
 * Java 21 Record for Service Stock Response - immutable DTO
 */
public record ServiceStockResponse(
    Long id,
    String name,
    BigDecimal price,
    Long garageId,
    String garageName
) {}
