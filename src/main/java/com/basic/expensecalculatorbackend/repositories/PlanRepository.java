package com.basic.expensecalculatorbackend.repositories;

import com.basic.expensecalculatorbackend.data.entities.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    Optional<PlanEntity> findByProfileIdAndIsActiveTrue(Long profileId);
}
