package com.garage_guru.repository;

import com.garage_guru.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByGarageId(Long garageId);

    List<Invoice> findByGarageIdOrderByInvoiceDateDesc(Long garageId);

    List<Invoice> findByGarageIdAndStatus(Long garageId, String status);

    List<Invoice> findByGarageIdAndPaymentStatus(Long garageId, String paymentStatus);

    List<Invoice> findByGarageIdAndInvoiceDateBetween(Long garageId, LocalDate startDate, LocalDate endDate);

    List<Invoice> findByGarageIdAndInvoiceDate(Long garageId, LocalDate invoiceDate);

    List<Invoice> findByJobCardId(Long jobCardId);

    List<Invoice> findByGarageIdAndCustomerNameContainingIgnoreCase(Long garageId, String customerName);

    List<Invoice> findByGarageIdAndVehicleNumberContainingIgnoreCase(Long garageId, String vehicleNumber);

    // Count invoices by garage
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.garage.id = :garageId")
    Long countByGarageId(@Param("garageId") Long garageId);

    // Count invoices by garage and status
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.garage.id = :garageId AND i.status = :status")
    Long countByGarageIdAndStatus(@Param("garageId") Long garageId, @Param("status") String status);

    // Count invoices by garage and payment status
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.garage.id = :garageId AND i.paymentStatus = :paymentStatus")
    Long countByGarageIdAndPaymentStatus(@Param("garageId") Long garageId, @Param("paymentStatus") String paymentStatus);

    // Total invoice amount by garage
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId")
    BigDecimal getTotalInvoiceAmountByGarageId(@Param("garageId") Long garageId);

    // Total paid amount by garage
    @Query("SELECT COALESCE(SUM(i.paidAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId")
    BigDecimal getTotalPaidAmountByGarageId(@Param("garageId") Long garageId);

    // Total due amount by garage
    @Query("SELECT COALESCE(SUM(i.dueAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId")
    BigDecimal getTotalDueAmountByGarageId(@Param("garageId") Long garageId);

    // Today's invoices total
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId AND i.invoiceDate = :today")
    BigDecimal getTodayInvoiceTotalByGarageId(@Param("garageId") Long garageId, @Param("today") LocalDate today);

    // Invoice total by date range
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId AND i.invoiceDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalInvoiceAmountByGarageIdAndDateRange(
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Count invoices by date range
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.garage.id = :garageId AND i.invoiceDate BETWEEN :startDate AND :endDate")
    Long countByGarageIdAndDateRange(
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Monthly invoices
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId AND YEAR(i.invoiceDate) = :year AND MONTH(i.invoiceDate) = :month")
    BigDecimal getMonthlyInvoiceTotalByGarageId(
            @Param("garageId") Long garageId,
            @Param("year") int year,
            @Param("month") int month
    );

    // Yearly invoices
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId AND YEAR(i.invoiceDate) = :year")
    BigDecimal getYearlyInvoiceTotalByGarageId(@Param("garageId") Long garageId, @Param("year") int year);

    // Get max invoice number for generating new one
    @Query("SELECT MAX(i.invoiceNumber) FROM Invoice i WHERE i.garage.id = :garageId")
    String getMaxInvoiceNumberByGarageId(@Param("garageId") Long garageId);

    // Overdue invoices
    @Query("SELECT i FROM Invoice i WHERE i.garage.id = :garageId AND i.dueDate < :today AND i.paymentStatus != 'PAID'")
    List<Invoice> findOverdueInvoices(@Param("garageId") Long garageId, @Param("today") LocalDate today);

    // Count overdue invoices
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.garage.id = :garageId AND i.dueDate < :today AND i.paymentStatus != 'PAID'")
    Long countOverdueInvoices(@Param("garageId") Long garageId, @Param("today") LocalDate today);

    // Total overdue amount
    @Query("SELECT COALESCE(SUM(i.dueAmount), 0) FROM Invoice i WHERE i.garage.id = :garageId AND i.dueDate < :today AND i.paymentStatus != 'PAID'")
    BigDecimal getTotalOverdueAmount(@Param("garageId") Long garageId, @Param("today") LocalDate today);
}
