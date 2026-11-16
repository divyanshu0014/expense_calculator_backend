package com.basic.expensecalculatorbackend.mapper;

import com.basic.expensecalculatorbackend.data.dtos.plan.PlanAllocationResponse;
import com.basic.expensecalculatorbackend.data.entities.PlanAllocationEntity;
import com.basic.expensecalculatorbackend.data.entities.PlanEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PlanAllocationMapper {

    public List<PlanAllocationResponse> toResponse(List<PlanAllocationEntity> allocations) {
        return allocations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PlanAllocationResponse toResponse(PlanAllocationEntity entity) {
        return PlanAllocationResponse.builder()
                .id(entity.getId())
                .planId(Objects.nonNull(entity.getPlan()) ? entity.getPlan().getId() : null)
                .name(entity.getName())
                .amountPaise(entity.getAmountPaise())
                .kind(entity.getKind())
                .dueDay(entity.getDueDay())
                .build();
    }

    public PlanAllocationEntity toEntity(PlanAllocationResponse planAllocationResponse, PlanEntity plan, PlanAllocationEntity existingEntity) {
        if (existingEntity == null) {
            existingEntity = new PlanAllocationEntity();
        }
        existingEntity.setPlan(plan);
        existingEntity.setName(planAllocationResponse.getName());
        existingEntity.setAmountPaise(planAllocationResponse.getAmountPaise());
        existingEntity.setKind(planAllocationResponse.getKind());
        existingEntity.setDueDay(planAllocationResponse.getDueDay());
        return existingEntity;
    }

}
