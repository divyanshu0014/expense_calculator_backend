package com.basic.expensecalculatorbackend.repositories;

import com.basic.expensecalculatorbackend.data.entities.CyclesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CycleRepository extends JpaRepository<CyclesEntity, Long> {
    Optional<CyclesEntity> findFirstByPlanIdOrderByStartDateDesc(Long planId);
}
