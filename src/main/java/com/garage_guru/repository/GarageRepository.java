package com.garage_guru.repository;

import com.garage_guru.entity.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    Optional<Garage> findByEmail(String email);
    Boolean existsByEmail(String email);
}
