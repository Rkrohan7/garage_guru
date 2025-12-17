package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceSummaryResponse(
    Long garageId,
    String garageName,

    // Counts
    Long totalInvoices,
    Long draftInvoices,
    Long sentInvoices,
    Long paidInvoices,
    Long partiallyPaidInvoices,
    Long overdueInvoices,
    Long cancelledInvoices,

    // Amounts
    BigDecimal totalInvoiceAmount,
    BigDecimal totalPaidAmount,
    BigDecimal totalDueAmount,
    BigDecimal totalOverdueAmount,

    // Period-wise
    BigDecimal todayInvoiceTotal,
    BigDecimal weekInvoiceTotal,
    BigDecimal monthInvoiceTotal,
    BigDecimal yearInvoiceTotal,

    // Date range (if applicable)
    LocalDate fromDate,
    LocalDate toDate
) {
    public static InvoiceSummaryResponse of(
            Long garageId, String garageName,
            Long totalInvoices, Long draftInvoices, Long sentInvoices,
            Long paidInvoices, Long partiallyPaidInvoices, Long overdueInvoices, Long cancelledInvoices,
            BigDecimal totalInvoiceAmount, BigDecimal totalPaidAmount, BigDecimal totalDueAmount, BigDecimal totalOverdueAmount,
            BigDecimal todayInvoiceTotal, BigDecimal weekInvoiceTotal, BigDecimal monthInvoiceTotal, BigDecimal yearInvoiceTotal,
            LocalDate fromDate, LocalDate toDate
    ) {
        return new InvoiceSummaryResponse(
                garageId, garageName,
                totalInvoices, draftInvoices, sentInvoices, paidInvoices, partiallyPaidInvoices, overdueInvoices, cancelledInvoices,
                totalInvoiceAmount != null ? totalInvoiceAmount : BigDecimal.ZERO,
                totalPaidAmount != null ? totalPaidAmount : BigDecimal.ZERO,
                totalDueAmount != null ? totalDueAmount : BigDecimal.ZERO,
                totalOverdueAmount != null ? totalOverdueAmount : BigDecimal.ZERO,
                todayInvoiceTotal != null ? todayInvoiceTotal : BigDecimal.ZERO,
                weekInvoiceTotal != null ? weekInvoiceTotal : BigDecimal.ZERO,
                monthInvoiceTotal != null ? monthInvoiceTotal : BigDecimal.ZERO,
                yearInvoiceTotal != null ? yearInvoiceTotal : BigDecimal.ZERO,
                fromDate, toDate
        );
    }
}
