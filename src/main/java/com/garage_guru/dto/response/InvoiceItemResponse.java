package com.garage_guru.dto.response;

import java.math.BigDecimal;

public record InvoiceItemResponse(
    Long id,
    String itemType,
    String itemName,
    String itemCode,
    String description,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal discountAmount,
    BigDecimal taxAmount,
    BigDecimal totalPrice,
    Long sparePartStockId,
    Long lubesStockId,
    Long serviceStockId
) {}