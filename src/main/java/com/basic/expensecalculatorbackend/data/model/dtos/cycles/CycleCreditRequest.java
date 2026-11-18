package com.basic.expensecalculatorbackend.data.model.dtos.cycles;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CycleCreditRequest {

    private BigDecimal extraCreditsPaise;
    // Released money from last month's savings for spending
    private BigDecimal releasedCarryPaise;

}
