package com.garage_guru.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AddItemsToJobCardRequest(
    @NotNull(message = "Items list is required")
    @Valid
    List<JobCardItemRequest> items,

    BigDecimal labourCharges,

    BigDecimal discountAmount,

    BigDecimal taxAmount
) {}