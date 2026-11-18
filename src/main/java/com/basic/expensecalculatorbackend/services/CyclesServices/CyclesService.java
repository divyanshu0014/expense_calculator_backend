package com.basic.expensecalculatorbackend.services.CyclesServices;

import com.basic.expensecalculatorbackend.data.model.dtos.cycles.CycleCreditRequest;
import com.basic.expensecalculatorbackend.data.model.dtos.cycles.CycleHistoryResponse;
import com.basic.expensecalculatorbackend.data.model.dtos.cycles.CycleResponse;
import com.basic.expensecalculatorbackend.data.model.dtos.cycles.StartNewCyclesRequest;
import com.basic.expensecalculatorbackend.data.model.entities.CyclesEntity;
import com.basic.expensecalculatorbackend.data.model.entities.PlanAllocationEntity;
import com.basic.expensecalculatorbackend.data.model.entities.PlanEntity;
import com.basic.expensecalculatorbackend.data.enums.SourceEnum;
import com.basic.expensecalculatorbackend.mapper.CycleMapper;
import com.basic.expensecalculatorbackend.repositories.CycleRepository;
import com.basic.expensecalculatorbackend.repositories.PlanAllocationRepository;
import com.basic.expensecalculatorbackend.repositories.PlanRepository;
import com.basic.expensecalculatorbackend.services.TransactionServices.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CyclesService {

    private final CycleRepository cyclesRepository;
    private final PlanRepository planRepository;
    private final CycleMapper cycleMapper;
    private final TransactionService transactionService;
    private final PlanAllocationRepository planAllocationRepository;

    public CyclesService(CycleRepository cyclesRepository, PlanRepository planRepository, CycleMapper cycleMapper, TransactionService transactionService, PlanAllocationRepository planAllocationRepository) {
        this.cyclesRepository = cyclesRepository;
        this.planRepository = planRepository;
        this.cycleMapper = cycleMapper;
        this.transactionService = transactionService;
        this.planAllocationRepository = planAllocationRepository;
    }

    @Transactional
    public CycleResponse startNewCycle(Long planId, StartNewCyclesRequest cyclesRequest) {
        PlanEntity activePlan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        CyclesEntity previousCycle = cyclesRepository.findFirstByPlanIdAndIsActiveTrueOrderByStartDateDesc(planId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found"));

        BigDecimal unSpentCarryOverPaise = calculateUnspentCarryOverPaise(activePlan, previousCycle);
        String label = null;
        if (cyclesRequest.getLabel() == null || cyclesRequest.getLabel().isBlank()) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM");
            // e.g. "25 Oct – 24 Nov" (end date minus 1 day is the last day of the cycle)
            label = previousCycle.getEndDate().format(fmt) + " – " + previousCycle.getEndDate().plusMonths(1).minusDays(1).format(fmt);
        }

        CycleResponse newCycle = CycleResponse.builder()
                .planId(planId)
                .startDate(previousCycle.getEndDate())
                .endDate(previousCycle.getEndDate().plusMonths(1))
                .takeHomePaise(cyclesRequest.getTakeHomePaise() != null ? cyclesRequest.getTakeHomePaise() : activePlan.getTakeHomePaise())
                .openingCarryPaise(unSpentCarryOverPaise)
                .extraCreditsPaise(cyclesRequest.getExtraCreditsPaise() != null ? cyclesRequest.getExtraCreditsPaise() : BigDecimal.ZERO)
                .releasedCarryPaise(cyclesRequest.getReleasedCarryPaise() != null ? cyclesRequest.getReleasedCarryPaise() : BigDecimal.ZERO)
                .label(cyclesRequest.getLabel() != null ? cyclesRequest.getLabel() : label)
                .source(SourceEnum.APP)
                .sheetBalancePaise(previousCycle.getSheetBalancePaise() != null ? previousCycle.getSheetBalancePaise() : BigDecimal.ZERO)
                .build();

        previousCycle.setActive(false);
        cyclesRepository.save(previousCycle);

        CyclesEntity savedCycle = cyclesRepository.save(cycleMapper.responseToCycleEntity(newCycle, activePlan, null));
        newCycle.setCycleId(savedCycle.getId());

        // Logic for starting a new cycle would go here
        return newCycle;
    }

    private BigDecimal calculateUnspentCarryOverPaise(PlanEntity plan, CyclesEntity previousCycle) {
        BigDecimal allocated = getAllocatedPaise(plan.getId());
        BigDecimal spent = transactionService.calculateSpent(previousCycle);
        BigDecimal saved = calculateSaved(previousCycle, allocated, spent);
        BigDecimal opening = previousCycle.getOpeningCarryPaise() != null
                ? previousCycle.getOpeningCarryPaise()
                : BigDecimal.ZERO;
        return opening.add(saved);
    }


    @Transactional
    public CycleResponse getCurrentActiveCycle(Long planId) {
       CyclesEntity activeCycle =  cyclesRepository.findFirstByPlanIdAndIsActiveTrueOrderByStartDateDesc(planId)
               .orElseThrow(() -> new IllegalArgumentException("No Active Cycle found for the Plan " + planId));

        return cycleMapper.entityToCycleResponse(activeCycle);
    }

    public List<CycleHistoryResponse> getAllCycles(Long planId) {
        List<CyclesEntity> cycles = cyclesRepository.findByPlanIdOrderByStartDateDesc(planId);
        BigDecimal allocated = getAllocatedPaise(planId);
        return cycles.stream()
                .map(cycle -> {
                    BigDecimal spent = transactionService.calculateSpent(cycle);
                    BigDecimal saved = calculateSaved(cycle, allocated, spent);
                    BigDecimal closingBalance = cycle.getOpeningCarryPaise().add(saved);

                    return cycleMapper.entityToCycleHistoryResponse(cycle, allocated, spent, closingBalance, saved);
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateSaved(CyclesEntity cycle, BigDecimal allocated, BigDecimal spent) {
        BigDecimal spendable = cycle.getTakeHomePaise()
                .add(cycle.getExtraCreditsPaise()!=null? cycle.getExtraCreditsPaise():BigDecimal.ZERO).subtract(allocated);
        return spendable.subtract(spent);
    }

    private BigDecimal getAllocatedPaise(Long planId) {
        return planAllocationRepository.findAllocationsByPlanId(planId).stream()
                .map(PlanAllocationEntity::getAmountPaise)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public CycleResponse addCredits(Long cycleId, CycleCreditRequest cycleCreditRequest) {
        CyclesEntity currentCycle = cyclesRepository.findById(cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle ID " + cycleId + " does not exist"));
        // 1. If request has extraCreditsPaise, set it as the new value
        if (cycleCreditRequest.getExtraCreditsPaise() != null) {
            currentCycle.setExtraCreditsPaise(cycleCreditRequest.getExtraCreditsPaise());
        }
        // 2. If request has releasedCarryPaise, validate against reserve and set it
        if (cycleCreditRequest.getReleasedCarryPaise() != null) {
            BigDecimal openingReserve = currentCycle.getOpeningCarryPaise() != null
                    ? currentCycle.getOpeningCarryPaise()
                    : BigDecimal.ZERO;
            if (cycleCreditRequest.getReleasedCarryPaise().compareTo(openingReserve) > 0) {
                throw new IllegalArgumentException(
                        "Cannot release more than available carry-over reserve of " + openingReserve + " paise"
                );
            }
            currentCycle.setReleasedCarryPaise(cycleCreditRequest.getReleasedCarryPaise());
        }
        return cycleMapper.entityToCycleResponse(cyclesRepository.save(currentCycle));
    }

}
