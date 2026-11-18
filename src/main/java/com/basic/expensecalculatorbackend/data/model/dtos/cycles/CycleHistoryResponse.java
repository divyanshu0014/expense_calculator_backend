package com.basic.expensecalculatorbackend.data.model.dtos.cycles;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CycleHistoryResponse {
        private Long cycleId;
        private String label;              // e.g. "25 Oct – 24 Nov"
        private LocalDate startDate;
        private LocalDate endDate;

        // Financial Breakdown
        private BigDecimal openingCarryPaise; // Started with
        private BigDecimal takeHomePaise;     // Salary
        private BigDecimal extraCreditsPaise; // Bonus
        private BigDecimal allocatedPaise;    // Fixed bills (Rent, SIP)
        private BigDecimal spentPaise;        // Variable daily spending

        // The Closed Balances
        private BigDecimal closingBalancePaise; // Final money in bank at cycle close
        private BigDecimal savedPaise;          // Surplus saved (Spendable - Spent)

}
