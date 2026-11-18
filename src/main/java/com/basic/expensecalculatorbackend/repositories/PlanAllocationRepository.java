package com.basic.expensecalculatorbackend.repositories;

import com.basic.expensecalculatorbackend.data.model.entities.PlanAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanAllocationRepository extends JpaRepository<PlanAllocationEntity, Long> {
    List<PlanAllocationEntity> findAllocationsByPlanId(Long id);
    Optional<PlanAllocationEntity> findById(Long id);
}
