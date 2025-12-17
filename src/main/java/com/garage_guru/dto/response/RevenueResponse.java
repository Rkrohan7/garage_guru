package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Java 21 Record for Revenue Response - immutable DTO
 */
public record RevenueResponse(
    Long garageId,
    String garageName,
    BigDecimal totalRevenue,
    BigDecimal todayRevenue,
    BigDecimal weekRevenue,
    BigDecimal monthRevenue,
    BigDecimal yearRevenue,
    Long totalTransactions,
    Long todayTransactions,
    BigDecimal serviceRevenue,
    BigDecimal sparePartRevenue,
    BigDecimal lubeRevenue,
    BigDecimal otherRevenue,
    BigDecimal cashRevenue,
    BigDecimal cardRevenue,
    BigDecimal upiRevenue,
    BigDecimal bankTransferRevenue,
    LocalDate fromDate,
    LocalDate toDate
) {
    // Builder-like static factory methods for convenience
    public static RevenueResponse of(
            Long garageId,
            String garageName,
            BigDecimal totalRevenue,
            BigDecimal todayRevenue,
            BigDecimal weekRevenue,
            BigDecimal monthRevenue,
            BigDecimal yearRevenue,
            Long totalTransactions,
            Long todayTransactions,
            BigDecimal serviceRevenue,
            BigDecimal sparePartRevenue,
            BigDecimal lubeRevenue,
            BigDecimal otherRevenue,
            BigDecimal cashRevenue,
            BigDecimal cardRevenue,
            BigDecimal upiRevenue,
            BigDecimal bankTransferRevenue,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return new RevenueResponse(
                garageId, garageName, totalRevenue, todayRevenue, weekRevenue,
                monthRevenue, yearRevenue, totalTransactions, todayTransactions,
                serviceRevenue, sparePartRevenue, lubeRevenue, otherRevenue,
                cashRevenue, cardRevenue, upiRevenue, bankTransferRevenue,
                fromDate, toDate
        );
    }
}
