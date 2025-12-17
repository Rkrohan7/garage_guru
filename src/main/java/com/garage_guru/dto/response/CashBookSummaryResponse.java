package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CashBookSummaryResponse(
        Long garageId,
        String garageName,
        BigDecimal totalCredit,
        BigDecimal totalDebit,
        BigDecimal netBalance,
        Long totalCreditTransactions,
        Long totalDebitTransactions,
        LocalDate fromDate,
        LocalDate toDate
) {
    public static CashBookSummaryResponse of(
            Long garageId,
            String garageName,
            BigDecimal totalCredit,
            BigDecimal totalDebit,
            BigDecimal netBalance,
            Long totalCreditTransactions,
            Long totalDebitTransactions
    ) {
        return new CashBookSummaryResponse(
                garageId,
                garageName,
                totalCredit,
                totalDebit,
                netBalance,
                totalCreditTransactions,
                totalDebitTransactions,
                null,
                null
        );
    }

    public static CashBookSummaryResponse ofDateRange(
            Long garageId,
            String garageName,
            BigDecimal totalCredit,
            BigDecimal totalDebit,
            BigDecimal netBalance,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return new CashBookSummaryResponse(
                garageId,
                garageName,
                totalCredit,
                totalDebit,
                netBalance,
                null,
                null,
                fromDate,
                toDate
        );
    }
}