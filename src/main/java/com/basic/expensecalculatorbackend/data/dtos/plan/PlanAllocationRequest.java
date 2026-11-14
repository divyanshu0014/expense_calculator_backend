package com.basic.expensecalculatorbackend.data.dtos.plan;

import java.math.BigDecimal;

import com.basic.expensecalculatorbackend.data.enums.AllocationKindEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanAllocationRequest {

    @NotBlank(message = "Allocation name is required")
    private String name;

    @NotNull(message = "Allocation kind is required (FIXED, INVESTMENT, RESERVED)")
    private AllocationKindEnum kind;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amountPaise;

    @Min(1)
    @Max(31)
    private Integer dueDay;
}
