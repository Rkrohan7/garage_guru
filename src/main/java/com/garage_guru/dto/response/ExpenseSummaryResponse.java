package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Java 21 Record for Expense Summary Response - immutable DTO
 */
public record ExpenseSummaryResponse(
    Long garageId,
    String garageName,
    BigDecimal totalExpenses,
    BigDecimal todayExpenses,
    BigDecimal weekExpenses,
    BigDecimal monthExpenses,
    BigDecimal yearExpenses,
    Long totalTransactions,
    Long todayTransactions,
    // Category-wise expenses
    BigDecimal rentExpenses,
    BigDecimal salaryExpenses,
    BigDecimal utilitiesExpenses,
    BigDecimal maintenanceExpenses,
    BigDecimal inventoryExpenses,
    BigDecimal fuelExpenses,
    BigDecimal toolsExpenses,
    BigDecimal marketingExpenses,
    BigDecimal insuranceExpenses,
    BigDecimal taxExpenses,
    BigDecimal otherExpenses,
    // Payment method-wise expenses
    BigDecimal cashExpenses,
    BigDecimal cardExpenses,
    BigDecimal upiExpenses,
    BigDecimal bankTransferExpenses,
    LocalDate fromDate,
    LocalDate toDate
) {
    public static ExpenseSummaryResponse of(
            Long garageId, String garageName,
            BigDecimal totalExpenses, BigDecimal todayExpenses, BigDecimal weekExpenses,
            BigDecimal monthExpenses, BigDecimal yearExpenses,
            Long totalTransactions, Long todayTransactions,
            BigDecimal rentExpenses, BigDecimal salaryExpenses, BigDecimal utilitiesExpenses,
            BigDecimal maintenanceExpenses, BigDecimal inventoryExpenses, BigDecimal fuelExpenses,
            BigDecimal toolsExpenses, BigDecimal marketingExpenses, BigDecimal insuranceExpenses,
            BigDecimal taxExpenses, BigDecimal otherExpenses,
            BigDecimal cashExpenses, BigDecimal cardExpenses, BigDecimal upiExpenses,
            BigDecimal bankTransferExpenses,
            LocalDate fromDate, LocalDate toDate
    ) {
        return new ExpenseSummaryResponse(
                garageId, garageName, totalExpenses, todayExpenses, weekExpenses,
                monthExpenses, yearExpenses, totalTransactions, todayTransactions,
                rentExpenses, salaryExpenses, utilitiesExpenses, maintenanceExpenses,
                inventoryExpenses, fuelExpenses, toolsExpenses, marketingExpenses,
                insuranceExpenses, taxExpenses, otherExpenses,
                cashExpenses, cardExpenses, upiExpenses, bankTransferExpenses,
                fromDate, toDate
        );
    }
}