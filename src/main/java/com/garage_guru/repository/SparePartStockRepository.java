package com.garage_guru.repository;

import com.garage_guru.entity.SparePartStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SparePartStockRepository extends JpaRepository<SparePartStock, Long> {
    List<SparePartStock> findByGarageId(Long garageId);
    List<SparePartStock> findByGarageIdAndStockQuantityLessThanEqual(Long garageId, Integer quantity);
}
