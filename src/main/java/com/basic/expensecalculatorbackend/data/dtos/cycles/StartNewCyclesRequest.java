package com.basic.expensecalculatorbackend.data.dtos.cycles;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StartNewCyclesRequest {
    // Salary
    private BigDecimal takeHomePaise;
    // Any Additional Income
    private BigDecimal extraCreditsPaise;
    // Released money from last month's savings for spending
    private BigDecimal releasedCarryPaise;

    private String label;
}
