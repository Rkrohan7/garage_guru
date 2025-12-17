package com.garage_guru.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_card_id")
    private JobCard jobCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    // Customer Details
    @Column(nullable = false)
    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private String customerAddress;

    // Vehicle Details
    private String vehicleNumber;

    private String vehicleMake;

    private String vehicleModel;

    // Invoice Items
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    // Pricing
    @Column(nullable = false)
    private BigDecimal subtotal;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxPercentage = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal dueAmount = BigDecimal.ZERO;

    // Status
    @Builder.Default
    private String status = "DRAFT";  // DRAFT, SENT, PAID, PARTIALLY_PAID, OVERDUE, CANCELLED

    @Builder.Default
    private String paymentStatus = "UNPAID";  // UNPAID, PARTIAL, PAID

    // Dates
    @Column(nullable = false)
    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private LocalDate paidDate;

    // Notes
    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    // Audit
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (invoiceDate == null) {
            invoiceDate = LocalDate.now();
        }
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void calculateTotals() {
        if (subtotal == null) subtotal = BigDecimal.ZERO;
        if (discountAmount == null) discountAmount = BigDecimal.ZERO;
        if (taxAmount == null) taxAmount = BigDecimal.ZERO;
        if (paidAmount == null) paidAmount = BigDecimal.ZERO;

        // Calculate total
        totalAmount = subtotal.subtract(discountAmount).add(taxAmount);

        // Calculate due amount
        dueAmount = totalAmount.subtract(paidAmount);

        // Update payment status
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            paymentStatus = "UNPAID";
        } else if (paidAmount.compareTo(totalAmount) >= 0) {
            paymentStatus = "PAID";
            dueAmount = BigDecimal.ZERO;
        } else {
            paymentStatus = "PARTIAL";
        }
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
    }

    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
    }
}