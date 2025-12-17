package com.garage_guru.repository;

import com.garage_guru.entity.JobCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCardRepository extends JpaRepository<JobCard, Long> {
    List<JobCard> findByGarageId(Long garageId);
    List<JobCard> findByGarageIdAndFlag(Long garageId, Boolean flag);
    List<JobCard> findByVehicleNumberPlateContainingIgnoreCase(String vehicleNumberPlate);
    List<JobCard> findByGarageIdAndStatus(Long garageId, String status);
    List<JobCard> findByStatus(String status);
}
