package com.garage_guru.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleType;

    private String vehicleNumberPlate;

    private String make;

    private String model;

    private String variant;

    private Integer kmReading;

    private String fuelLevel;

    @Column(columnDefinition = "TEXT")
    private String jobNotes;

    @Builder.Default
    private Boolean flag = false;

    @Builder.Default
    private String status = "PENDING";  // PENDING, IN_PROGRESS, COMPLETED

    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private String customerAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    // Job Card Items (spare parts, lubes, services used)
    @OneToMany(mappedBy = "jobCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JobCardItem> items = new ArrayList<>();

    // Pricing summary
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal labourCharges = BigDecimal.ZERO;

    // Timestamps
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addItem(JobCardItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setJobCard(this);
        // Ensure totalPrice is calculated
        if (item.getTotalPrice() == null) {
            item.calculateTotal();
        }
        calculateTotals();
    }

    public void removeItem(JobCardItem item) {
        items.remove(item);
        item.setJobCard(null);
        calculateTotals();
    }

    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            subtotal = BigDecimal.ZERO;
        } else {
            subtotal = items.stream()
                    .map(item -> item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        if (labourCharges == null) labourCharges = BigDecimal.ZERO;
        if (discountAmount == null) discountAmount = BigDecimal.ZERO;
        if (taxAmount == null) taxAmount = BigDecimal.ZERO;

        totalAmount = subtotal.add(labourCharges).subtract(discountAmount).add(taxAmount);
    }
}
