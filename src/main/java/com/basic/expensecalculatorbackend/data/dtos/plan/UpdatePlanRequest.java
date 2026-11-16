package com.basic.expensecalculatorbackend.data.dtos.plan;

import com.basic.expensecalculatorbackend.data.enums.CurrencyEnum;
import com.basic.expensecalculatorbackend.data.enums.MotionEnum;
import com.basic.expensecalculatorbackend.data.enums.PayFrequencyEnum;
import com.basic.expensecalculatorbackend.data.enums.ThemeEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePlanRequest {
    @Min(1) @Max(31)
    private Long salaryDay;
    @Positive
    private BigDecimal takeHomePaise;
    private BigDecimal releasedCarryPaise;
    private PayFrequencyEnum payFrequency;
    private CurrencyEnum currency;
    private ThemeEnum theme;
    private MotionEnum motion;
    private Boolean tourSeen;
}
