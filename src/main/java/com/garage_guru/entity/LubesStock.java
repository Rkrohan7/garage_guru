package com.garage_guru.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "lubes_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LubesStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    private String manufacturer;

    private String partNo;

    @Column(nullable = false)
    private Integer stockQuantity;

    private Integer minStockAlert;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;
}
