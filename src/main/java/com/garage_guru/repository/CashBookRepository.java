package com.garage_guru.repository;

import com.garage_guru.entity.CashBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Long> {

    List<CashBook> findByGarageIdOrderByTransactionDateDescCreatedAtDesc(Long garageId);

    List<CashBook> findByGarageIdAndTransactionTypeOrderByTransactionDateDescCreatedAtDesc(Long garageId, String transactionType);

    List<CashBook> findByGarageIdAndCategoryOrderByTransactionDateDescCreatedAtDesc(Long garageId, String category);

    List<CashBook> findByGarageIdAndTransactionDateOrderByCreatedAtDesc(Long garageId, LocalDate transactionDate);

    List<CashBook> findByGarageIdAndTransactionDateBetweenOrderByTransactionDateDescCreatedAtDesc(
            Long garageId, LocalDate startDate, LocalDate endDate);

    // Total Credit (Income) for a garage
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c WHERE c.garage.id = :garageId AND c.transactionType = 'CREDIT'")
    BigDecimal getTotalCredit(@Param("garageId") Long garageId);

    // Total Debit (Expense) for a garage
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c WHERE c.garage.id = :garageId AND c.transactionType = 'DEBIT'")
    BigDecimal getTotalDebit(@Param("garageId") Long garageId);

    // Net Balance (Credit - Debit)
    @Query("SELECT COALESCE(SUM(CASE WHEN c.transactionType = 'CREDIT' THEN c.amount ELSE -c.amount END), 0) " +
           "FROM CashBook c WHERE c.garage.id = :garageId")
    BigDecimal getNetBalance(@Param("garageId") Long garageId);

    // Credit by date range
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'CREDIT' " +
           "AND c.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalCreditByDateRange(@Param("garageId") Long garageId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    // Debit by date range
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'DEBIT' " +
           "AND c.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalDebitByDateRange(@Param("garageId") Long garageId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    // Net by date range
    @Query("SELECT COALESCE(SUM(CASE WHEN c.transactionType = 'CREDIT' THEN c.amount ELSE -c.amount END), 0) " +
           "FROM CashBook c WHERE c.garage.id = :garageId " +
           "AND c.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getNetBalanceByDateRange(@Param("garageId") Long garageId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    // Today's totals
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'CREDIT' " +
           "AND c.transactionDate = :today")
    BigDecimal getTodayCredit(@Param("garageId") Long garageId, @Param("today") LocalDate today);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'DEBIT' " +
           "AND c.transactionDate = :today")
    BigDecimal getTodayDebit(@Param("garageId") Long garageId, @Param("today") LocalDate today);

    // Credit by category
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'CREDIT' AND c.category = :category")
    BigDecimal getTotalCreditByCategory(@Param("garageId") Long garageId, @Param("category") String category);

    // Debit by category
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'DEBIT' AND c.category = :category")
    BigDecimal getTotalDebitByCategory(@Param("garageId") Long garageId, @Param("category") String category);

    // Count transactions
    @Query("SELECT COUNT(c) FROM CashBook c WHERE c.garage.id = :garageId AND c.transactionType = 'CREDIT'")
    Long countCreditTransactions(@Param("garageId") Long garageId);

    @Query("SELECT COUNT(c) FROM CashBook c WHERE c.garage.id = :garageId AND c.transactionType = 'DEBIT'")
    Long countDebitTransactions(@Param("garageId") Long garageId);

    // Search
    List<CashBook> findByGarageIdAndPartyNameContainingIgnoreCaseOrderByTransactionDateDesc(Long garageId, String partyName);

    List<CashBook> findByGarageIdAndDescriptionContainingIgnoreCaseOrderByTransactionDateDesc(Long garageId, String description);

    // By reference entities
    List<CashBook> findByInvoiceId(Long invoiceId);

    List<CashBook> findByPaymentId(Long paymentId);

    List<CashBook> findByExpenseId(Long expenseId);

    List<CashBook> findByJobCardId(Long jobCardId);

    // Monthly summary queries
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'CREDIT' " +
           "AND YEAR(c.transactionDate) = :year AND MONTH(c.transactionDate) = :month")
    BigDecimal getMonthlyCreditTotal(@Param("garageId") Long garageId,
                                      @Param("year") int year,
                                      @Param("month") int month);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashBook c " +
           "WHERE c.garage.id = :garageId AND c.transactionType = 'DEBIT' " +
           "AND YEAR(c.transactionDate) = :year AND MONTH(c.transactionDate) = :month")
    BigDecimal getMonthlyDebitTotal(@Param("garageId") Long garageId,
                                     @Param("year") int year,
                                     @Param("month") int month);
}