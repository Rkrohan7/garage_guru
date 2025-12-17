package com.garage_guru.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @Column(nullable = false)
    private String transactionType;  // CREDIT (income) or DEBIT (expense)

    @Column(nullable = false)
    private String category;
    // CREDIT categories: INVOICE_PAYMENT, SERVICE_PAYMENT, SPARE_PART_SALE, LUBE_SALE, OTHER_INCOME
    // DEBIT categories: EXPENSE, SALARY, RENT, UTILITIES, INVENTORY_PURCHASE, FUEL, TOOLS, MARKETING, INSURANCE, TAX, OTHER_EXPENSE

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    private String paymentMethod;  // CASH, CARD, UPI, BANK_TRANSFER, CHEQUE

    private String referenceNumber;  // Invoice number, receipt number, etc.

    private String partyName;  // Customer name or vendor name

    private String partyPhone;

    // Optional references to related entities
    private Long invoiceId;

    private Long paymentId;

    private Long expenseId;

    private Long jobCardId;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Builder.Default
    private BigDecimal runningBalance = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (transactionDate == null) {
            transactionDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}