package com.garage_guru.repository;

import com.garage_guru.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByGarageId(Long garageId);

    List<Payment> findByGarageIdAndPaymentDateBetween(Long garageId, LocalDate startDate, LocalDate endDate);

    List<Payment> findByGarageIdAndPaymentDate(Long garageId, LocalDate paymentDate);

    List<Payment> findByGarageIdAndPaymentType(Long garageId, String paymentType);

    List<Payment> findByGarageIdAndPaymentMethod(Long garageId, String paymentMethod);

    List<Payment> findByJobCardId(Long jobCardId);

    // Total revenue by garage
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.garage.id = :garageId")
    BigDecimal getTotalRevenueByGarageId(@Param("garageId") Long garageId);

    // Total revenue by garage and date range
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.garage.id = :garageId AND p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueByGarageIdAndDateRange(
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Total revenue by garage and payment type
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.garage.id = :garageId AND p.paymentType = :paymentType")
    BigDecimal getTotalRevenueByGarageIdAndPaymentType(
            @Param("garageId") Long garageId,
            @Param("paymentType") String paymentType
    );

    // Today's revenue
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.garage.id = :garageId AND p.paymentDate = :today")
    BigDecimal getTodayRevenueByGarageId(@Param("garageId") Long garageId, @Param("today") LocalDate today);

    // Count payments by garage
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.garage.id = :garageId")
    Long countByGarageId(@Param("garageId") Long garageId);

    // Count payments by garage and date range
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.garage.id = :garageId AND p.paymentDate BETWEEN :startDate AND :endDate")
    Long countByGarageIdAndDateRange(
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Revenue by payment method
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.garage.id = :garageId AND p.paymentMethod = :paymentMethod")
    BigDecimal getTotalRevenueByGarageIdAndPaymentMethod(
            @Param("garageId") Long garageId,
            @Param("paymentMethod") String paymentMethod
    );

    // Monthly revenue
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.garage.id = :garageId AND YEAR(p.paymentDate) = :year AND MONTH(p.paymentDate) = :month")
    BigDecimal getMonthlyRevenueByGarageId(
            @Param("garageId") Long garageId,
            @Param("year") int year,
            @Param("month") int month
    );

    // Yearly revenue
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.garage.id = :garageId AND YEAR(p.paymentDate) = :year")
    BigDecimal getYearlyRevenueByGarageId(@Param("garageId") Long garageId, @Param("year") int year);
}
