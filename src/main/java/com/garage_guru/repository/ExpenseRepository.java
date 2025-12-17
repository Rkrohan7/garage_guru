package com.garage_guru.repository;

import com.garage_guru.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByGarageId(Long garageId);

    List<Expense> findByGarageIdOrderByExpenseDateDesc(Long garageId);

    List<Expense> findByGarageIdAndExpenseDateBetween(Long garageId, LocalDate startDate, LocalDate endDate);

    List<Expense> findByGarageIdAndExpenseDate(Long garageId, LocalDate expenseDate);

    List<Expense> findByGarageIdAndCategory(Long garageId, String category);

    List<Expense> findByGarageIdAndPaymentMethod(Long garageId, String paymentMethod);

    // Total expenses by garage
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.garage.id = :garageId")
    BigDecimal getTotalExpensesByGarageId(@Param("garageId") Long garageId);

    // Total expenses by garage and date range
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.garage.id = :garageId AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalExpensesByGarageIdAndDateRange(
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Total expenses by garage and category
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.garage.id = :garageId AND e.category = :category")
    BigDecimal getTotalExpensesByGarageIdAndCategory(
            @Param("garageId") Long garageId,
            @Param("category") String category
    );

    // Today's expenses
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.garage.id = :garageId AND e.expenseDate = :today")
    BigDecimal getTodayExpensesByGarageId(@Param("garageId") Long garageId, @Param("today") LocalDate today);

    // Count expenses by garage
    @Query("SELECT COUNT(e) FROM Expense e WHERE e.garage.id = :garageId")
    Long countByGarageId(@Param("garageId") Long garageId);

    // Count expenses by garage and date range
    @Query("SELECT COUNT(e) FROM Expense e WHERE e.garage.id = :garageId AND e.expenseDate BETWEEN :startDate AND :endDate")
    Long countByGarageIdAndDateRange(
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Monthly expenses
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.garage.id = :garageId AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month")
    BigDecimal getMonthlyExpensesByGarageId(
            @Param("garageId") Long garageId,
            @Param("year") int year,
            @Param("month") int month
    );

    // Yearly expenses
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.garage.id = :garageId AND YEAR(e.expenseDate) = :year")
    BigDecimal getYearlyExpensesByGarageId(@Param("garageId") Long garageId, @Param("year") int year);

    // Expenses by payment method
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.garage.id = :garageId AND e.paymentMethod = :paymentMethod")
    BigDecimal getTotalExpensesByGarageIdAndPaymentMethod(
            @Param("garageId") Long garageId,
            @Param("paymentMethod") String paymentMethod
    );
}
