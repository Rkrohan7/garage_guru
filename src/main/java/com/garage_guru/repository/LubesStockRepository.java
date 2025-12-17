package com.garage_guru.repository;

import com.garage_guru.entity.LubesStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LubesStockRepository extends JpaRepository<LubesStock, Long> {
    List<LubesStock> findByGarageId(Long garageId);
    List<LubesStock> findByGarageIdAndStockQuantityLessThanEqual(Long garageId, Integer quantity);
}
