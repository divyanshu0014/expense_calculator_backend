package com.basic.expensecalculatorbackend.services.TransactionServices;

import com.basic.expensecalculatorbackend.data.entities.CyclesEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    public BigDecimal calculateSpent(CyclesEntity cycle) {
        return BigDecimal.ZERO;
    }
}
