package com.garage_guru.dto.request;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

public record CompleteJobCardRequest(
    @Valid
    List<JobCardItemRequest> items,

    BigDecimal labourCharges,

    BigDecimal discountAmount,

    BigDecimal taxPercentage,

    BigDecimal taxAmount,

    String notes,

    // Invoice options
    Boolean generateInvoice,

    BigDecimal invoiceDiscountAmount,

    BigDecimal invoiceTaxPercentage,

    String invoiceNotes,

    String termsAndConditions,

    Integer dueDays  // Days until invoice is due
) {}