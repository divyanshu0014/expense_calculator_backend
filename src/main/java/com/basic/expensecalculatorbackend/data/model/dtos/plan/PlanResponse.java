package com.basic.expensecalculatorbackend.data.model.dtos.plan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.basic.expensecalculatorbackend.data.enums.CurrencyEnum;

import com.basic.expensecalculatorbackend.data.enums.MotionEnum;
import com.basic.expensecalculatorbackend.data.enums.PayFrequencyEnum;
import com.basic.expensecalculatorbackend.data.enums.ThemeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponse {

    //PlanFields
    private Long id;
    private Long userId;
    private Long salaryDay;
    private BigDecimal takeHomePaise;
    private CurrencyEnum currency;
    private PayFrequencyEnum payFrequency;
    private ThemeEnum theme;
    private MotionEnum motion;
    private Boolean tourSeen;
    private boolean isActive;

    //Allocations
    private List<PlanAllocationResponse> allocations;

    //Cycle
    private Long currentCycleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal spendablePoolPaise; // takehome-allocations

}
