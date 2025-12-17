package com.garage_guru.repository;

import com.garage_guru.entity.ServiceStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceStockRepository extends JpaRepository<ServiceStock, Long> {
    List<ServiceStock> findByGarageId(Long garageId);
}
