package com.garage_guru.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Java 21 Record for Cashbook Response - combines income and expenses
 */
public record CashbookResponse(
    Long garageId,
    String garageName,

    // Income (from payments)
    BigDecimal totalIncome,
    BigDecimal todayIncome,
    BigDecimal weekIncome,
    BigDecimal monthIncome,
    BigDecimal yearIncome,
    Long totalIncomeTransactions,

    // Expenses
    BigDecimal totalExpenses,
    BigDecimal todayExpenses,
    BigDecimal weekExpenses,
    BigDecimal monthExpenses,
    BigDecimal yearExpenses,
    Long totalExpenseTransactions,

    // Net calculations
    BigDecimal totalNet,
    BigDecimal todayNet,
    BigDecimal weekNet,
    BigDecimal monthNet,
    BigDecimal yearNet,

    // Profit indicators (true if positive)
    Boolean totalProfit,
    Boolean todayProfit,
    Boolean weekProfit,
    Boolean monthProfit,
    Boolean yearProfit,

    LocalDate fromDate,
    LocalDate toDate
) {
    public static CashbookResponse of(
            Long garageId, String garageName,
            BigDecimal totalIncome, BigDecimal todayIncome, BigDecimal weekIncome,
            BigDecimal monthIncome, BigDecimal yearIncome, Long totalIncomeTransactions,
            BigDecimal totalExpenses, BigDecimal todayExpenses, BigDecimal weekExpenses,
            BigDecimal monthExpenses, BigDecimal yearExpenses, Long totalExpenseTransactions,
            LocalDate fromDate, LocalDate toDate
    ) {
        // Calculate net values
        BigDecimal totalNet = totalIncome.subtract(totalExpenses);
        BigDecimal todayNet = todayIncome.subtract(todayExpenses);
        BigDecimal weekNet = weekIncome.subtract(weekExpenses);
        BigDecimal monthNet = monthIncome.subtract(monthExpenses);
        BigDecimal yearNet = yearIncome.subtract(yearExpenses);

        return new CashbookResponse(
                garageId, garageName,
                totalIncome, todayIncome, weekIncome, monthIncome, yearIncome, totalIncomeTransactions,
                totalExpenses, todayExpenses, weekExpenses, monthExpenses, yearExpenses, totalExpenseTransactions,
                totalNet, todayNet, weekNet, monthNet, yearNet,
                totalNet.compareTo(BigDecimal.ZERO) >= 0,
                todayNet.compareTo(BigDecimal.ZERO) >= 0,
                weekNet.compareTo(BigDecimal.ZERO) >= 0,
                monthNet.compareTo(BigDecimal.ZERO) >= 0,
                yearNet.compareTo(BigDecimal.ZERO) >= 0,
                fromDate, toDate
        );
    }
}
