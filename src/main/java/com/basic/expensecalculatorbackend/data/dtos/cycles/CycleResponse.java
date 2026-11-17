package com.basic.expensecalculatorbackend.data.dtos.cycles;

import com.basic.expensecalculatorbackend.data.enums.SourceEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CycleResponse {
    private Long cycleId;
    private Long planId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal takeHomePaise;
    private BigDecimal extraCreditsPaise;
    private BigDecimal openingCarryPaise;
    private BigDecimal releasedCarryPaise;
    private String label;
    private SourceEnum source;
    private BigDecimal sheetBalancePaise;
}
