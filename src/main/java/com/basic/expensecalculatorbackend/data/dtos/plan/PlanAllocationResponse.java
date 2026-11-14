package com.basic.expensecalculatorbackend.data.dtos.plan;

import java.math.BigDecimal;

import com.basic.expensecalculatorbackend.data.enums.AllocationKindEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanAllocationResponse {

    private Long id;

    private Long planId;

    private String name;

    private AllocationKindEnum kind;

    private BigDecimal amountPaise;

    private Integer dueDay;
}
