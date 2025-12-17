package com.garage_guru.repository;

import com.garage_guru.entity.JobCardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JobCardItemRepository extends JpaRepository<JobCardItem, Long> {

    List<JobCardItem> findByJobCardId(Long jobCardId);

    List<JobCardItem> findByJobCardIdAndItemType(Long jobCardId, String itemType);

    void deleteByJobCardId(Long jobCardId);

    @Query("SELECT COALESCE(SUM(i.totalPrice), 0) FROM JobCardItem i WHERE i.jobCard.id = :jobCardId")
    BigDecimal getTotalByJobCardId(@Param("jobCardId") Long jobCardId);

    @Query("SELECT COUNT(i) FROM JobCardItem i WHERE i.jobCard.id = :jobCardId")
    Long countByJobCardId(@Param("jobCardId") Long jobCardId);
}
