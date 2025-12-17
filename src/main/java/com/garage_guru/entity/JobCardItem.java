package com.garage_guru.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_card_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_card_id", nullable = false)
    private JobCard jobCard;

    @Column(nullable = false)
    private String itemType;  // SPARE_PART, LUBE, SERVICE, LABOUR, OTHER

    @Column(nullable = false)
    private String itemName;

    private String itemCode;

    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxPercentage = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    // Reference IDs for stock items
    private Long sparePartStockId;

    private Long lubesStockId;

    private Long serviceStockId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        calculateTotal();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateTotal();
    }

    public void calculateTotal() {
        if (quantity == null) quantity = 1;
        if (unitPrice == null) unitPrice = BigDecimal.ZERO;
        if (discountAmount == null) discountAmount = BigDecimal.ZERO;
        if (taxAmount == null) taxAmount = BigDecimal.ZERO;

        BigDecimal baseTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        totalPrice = baseTotal.subtract(discountAmount).add(taxAmount);
    }
}
