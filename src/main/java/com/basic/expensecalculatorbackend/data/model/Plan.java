package com.basic.expensecalculatorbackend.data.model;

import com.basic.expensecalculatorbackend.data.dtos.plan.PlanAllocationResponse;
import com.basic.expensecalculatorbackend.data.enums.CurrencyEnum;
import com.basic.expensecalculatorbackend.data.enums.MotionEnum;
import com.basic.expensecalculatorbackend.data.enums.PayFrequencyEnum;
import com.basic.expensecalculatorbackend.data.enums.ThemeEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Plan {
    private UserProfile userProfile;
    private Long salaryDay;
    private BigDecimal takeHomePaise;
    private PayFrequencyEnum payFrequency;
    private BigDecimal releasedCarryPaise;
    private CurrencyEnum currency;
    private ThemeEnum theme;
    private MotionEnum motion;
    private Boolean tourSeen;
    private List<PlanAllocationResponse> allocations;
    private Boolean isActive;
}
