package com.basic.expensecalculatorbackend.mapper;

import com.basic.expensecalculatorbackend.data.dtos.cycles.CycleHistoryResponse;
import com.basic.expensecalculatorbackend.data.dtos.cycles.CycleResponse;
import com.basic.expensecalculatorbackend.data.entities.CyclesEntity;
import com.basic.expensecalculatorbackend.data.entities.PlanEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CycleMapper {


    public CyclesEntity responseToCycleEntity(CycleResponse cycleResponse, PlanEntity plan, CyclesEntity existingCycle) {
        if(existingCycle == null){
            existingCycle = new CyclesEntity();
        }
        existingCycle.setPlan(plan);
        existingCycle.setStartDate(cycleResponse.getStartDate());
        existingCycle.setEndDate(cycleResponse.getEndDate());
        existingCycle.setTakeHomePaise(cycleResponse.getTakeHomePaise());
        existingCycle.setOpeningCarryPaise(cycleResponse.getOpeningCarryPaise());
        existingCycle.setReleasedCarryPaise(cycleResponse.getReleasedCarryPaise());
        existingCycle.setExtraCreditsPaise(cycleResponse.getExtraCreditsPaise());
        existingCycle.setLabel(cycleResponse.getLabel());
        existingCycle.setSource(cycleResponse.getSource());
        existingCycle.setSheetBalancePaise(cycleResponse.getSheetBalancePaise());

        return existingCycle;
    }

    public CycleResponse entityToCycleResponse(CyclesEntity cyclesEntity) {
        return  CycleResponse.builder()
                .cycleId(cyclesEntity.getId())
                .planId(cyclesEntity.getPlan().getId())
                .startDate(cyclesEntity.getStartDate())
                .endDate(cyclesEntity.getEndDate())
                .takeHomePaise(cyclesEntity.getTakeHomePaise())
                .openingCarryPaise(cyclesEntity.getOpeningCarryPaise())
                .releasedCarryPaise(cyclesEntity.getReleasedCarryPaise())
                .extraCreditsPaise(cyclesEntity.getExtraCreditsPaise())
                .label(cyclesEntity.getLabel())
                .source(cyclesEntity.getSource())
                .sheetBalancePaise(cyclesEntity.getSheetBalancePaise())
                .build();
    }

    public CycleHistoryResponse entityToCycleHistoryResponse(CyclesEntity cyclesEntity, BigDecimal allocatedPaise, BigDecimal spentPaise, BigDecimal closingBalance, BigDecimal saved) {
        return  CycleHistoryResponse.builder()
                .cycleId(cyclesEntity.getId())
                .startDate(cyclesEntity.getStartDate())
                .endDate(cyclesEntity.getEndDate())
                .takeHomePaise(cyclesEntity.getTakeHomePaise())
                .openingCarryPaise(cyclesEntity.getOpeningCarryPaise())
                .extraCreditsPaise(cyclesEntity.getExtraCreditsPaise())
                .allocatedPaise(allocatedPaise)
                .spentPaise(spentPaise)
                .closingBalancePaise(closingBalance)
                .savedPaise(saved)
                .label(cyclesEntity.getLabel())
                .build();
    }

}
