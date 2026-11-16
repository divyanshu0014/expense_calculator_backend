package com.basic.expensecalculatorbackend.data.dtos.plan;

import com.basic.expensecalculatorbackend.data.enums.CurrencyEnum;
import com.basic.expensecalculatorbackend.data.enums.MotionEnum;
import com.basic.expensecalculatorbackend.data.enums.PayFrequencyEnum;
import com.basic.expensecalculatorbackend.data.enums.ThemeEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePlanRequest {
    @NotNull(message = "Salary day is required")
    @Min(1)
    @Max(31)
    private Long salaryDay;

    @NotNull(message = "Take-home pay is required")
    @Positive(message = "Take-home pay must be greater than zero")
    private BigDecimal takeHomePaise;
    // Money currently in account to hold as reserve (optional)

    @NotNull(message = "Pay-Frequency should be mentioned")
    private PayFrequencyEnum payFrequency;

    private BigDecimal openingCarryPaise = BigDecimal.ZERO;

    @NotNull(message = "Currency is required")
    private CurrencyEnum currency = CurrencyEnum.INR;

    private ThemeEnum theme = ThemeEnum.SYSTEM;

    private MotionEnum motion = MotionEnum.STANDARD;

    private Boolean tourSeen = false;
    // Initial allocations (Rent, SIP, EMIs, etc.)
    private List<PlanAllocationRequest> allocations;

    private String label;

    private BigDecimal releasedCarryPaise;

}
