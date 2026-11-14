package com.basic.expensecalculatorbackend.repositories;

import com.basic.expensecalculatorbackend.data.entities.PlanAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanAllocationRepository extends JpaRepository<PlanAllocationEntity, Long> {
    List<PlanAllocationEntity> findByPlanId(Long id);
}
