package com.basic.expensecalculatorbackend.services.PlanServices;

import com.basic.expensecalculatorbackend.data.dtos.plan.CreatePlanRequest;
import com.basic.expensecalculatorbackend.data.dtos.plan.PlanResponse;
import com.basic.expensecalculatorbackend.data.dtos.plan.PlanAllocationRequest;
import com.basic.expensecalculatorbackend.data.dtos.plan.PlanAllocationResponse;
import com.basic.expensecalculatorbackend.data.entities.CyclesEntity;
import com.basic.expensecalculatorbackend.data.entities.PlanAllocationEntity;
import com.basic.expensecalculatorbackend.data.entities.PlanEntity;
import com.basic.expensecalculatorbackend.data.entities.UserProfileEntity;
import com.basic.expensecalculatorbackend.data.enums.SourceEnum;
import com.basic.expensecalculatorbackend.data.model.Plan;
import com.basic.expensecalculatorbackend.mapper.PlanAllocationMapper;
import com.basic.expensecalculatorbackend.mapper.PlanMapper;
import com.basic.expensecalculatorbackend.repositories.CycleRepository;
import com.basic.expensecalculatorbackend.repositories.PlanAllocationRepository;
import com.basic.expensecalculatorbackend.repositories.PlanRepository;
import com.basic.expensecalculatorbackend.repositories.UserProfileRepository;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final UserProfileRepository userProfileRepository;
    private final PlanAllocationRepository planAllocationRepository;
    private final PlanMapper planMapper;
    private final PlanAllocationMapper planAllocationMapper;
    private final CycleRepository cycleRepository;

    public PlanService(PlanRepository planRepository, UserProfileRepository userProfileRepository,
            PlanAllocationRepository planAllocationRepository, PlanMapper planMapper,
            PlanAllocationMapper planAllocationMapper, CycleRepository cycleRepository) {
        this.planRepository = planRepository;
        this.planAllocationRepository = planAllocationRepository;
        this.planMapper = planMapper;
        this.planAllocationMapper = planAllocationMapper;
        this.userProfileRepository = userProfileRepository;
        this.cycleRepository = cycleRepository;
    }

    @Transactional
    public PlanResponse createPlan(Long userId, CreatePlanRequest planRequest) {
        UserProfileEntity userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not Found"));

        Plan plan = getPlan(planRequest);

        PlanEntity savedPlan = planRepository.save(planMapper.toPlanEntity(plan, userProfile, null));
        List<PlanAllocationResponse> allocations = getSavedPlannedAllocation(savedPlan,
                planRequest.getAllocations());

        CyclesEntity initialCycle = createAndSaveInitialCycle(savedPlan, planRequest.getOpeningCarryPaise(),
                planRequest.getLabel());

        return PlanResponse.builder()
                .id(savedPlan.getId())
                .userId(userId)
                .currency(planRequest.getCurrency())
                .salaryDay(planRequest.getSalaryDay())
                .takeHomePaise(planRequest.getTakeHomePaise())
                .allocations(allocations)
                .currencyCycleId(initialCycle.getId())
                .startDate(initialCycle.getStartDate())
                .endDate(initialCycle.getEndDate())
                .spendablePoolPaise(savedPlan.getTakeHomePaise().subtract(getAllocationAmount(allocations)))
                .isActive(plan.getIsActive())
                .build();
    }

    private static @NonNull Plan getPlan(CreatePlanRequest planRequest) {
        Plan plan = new Plan();
        plan.setSalaryDay(planRequest.getSalaryDay());
        plan.setCurrency(planRequest.getCurrency());
        plan.setTheme(planRequest.getTheme());
        plan.setMotion(planRequest.getMotion());
        plan.setTourSeen(planRequest.getTourSeen());
        plan.setReleasedCarryPaise(planRequest.getReleasedCarryPaise());
        plan.setSalaryDay(planRequest.getSalaryDay());
        plan.setTakeHomePaise(planRequest.getTakeHomePaise());
        plan.setPayFrequency(planRequest.getPayFrequencyEnum());
        plan.setIsActive(true);
        return plan;
    }

    private BigDecimal getAllocationAmount(List<PlanAllocationResponse> allocations) {
        return allocations.stream()
                .map(PlanAllocationResponse::getAmountPaise)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CyclesEntity createAndSaveInitialCycle(PlanEntity savedPlan, BigDecimal openingCarryPaise, String label) {
        CyclesEntity initialCycle = new CyclesEntity();
        initialCycle.setPlan(savedPlan);
        initialCycle.setSource(SourceEnum.APP);
        initialCycle.setTakeHomePaise(savedPlan.getTakeHomePaise());

        initialCycle.setExtraCreditsPaise(BigDecimal.ZERO);
        initialCycle.setOpening_carry_paise(Objects.nonNull(openingCarryPaise) ? openingCarryPaise : BigDecimal.ZERO);
        initialCycle.setReleased_carry_paise(BigDecimal.ZERO);
        initialCycle.setLabel(label);

        LocalDate today = LocalDate.now();
        int  salaryDay = savedPlan.getSalaryDay().intValue();

        LocalDate cycleStart;
        LocalDate cycleEnd;

        if (today.getDayOfMonth() >= salaryDay) {
            cycleStart = clampDate(today.getYear(), today.getMonthValue(), salaryDay);
            LocalDate nextMonth = today.plusMonths(1);
            cycleEnd = clampDate(nextMonth.getYear(), nextMonth.getMonthValue(), salaryDay);
        } else {
            LocalDate prevMonth =  today.minusMonths(1);
            cycleStart = clampDate(prevMonth.getYear(), prevMonth.getMonthValue(), salaryDay);
            cycleEnd = clampDate(today.getYear(), today.getMonthValue(), salaryDay);
        }

        initialCycle.setStartDate(cycleStart);
        initialCycle.setEndDate(cycleEnd);

        return cycleRepository.save(initialCycle);

    }

    private LocalDate clampDate(int year, int month, int targetDay) {
        int maxDays = YearMonth.of(year, month).lengthOfMonth();
        return LocalDate.of(year, month, Math.min(targetDay, maxDays));
    }


    private List<PlanAllocationResponse> getSavedPlannedAllocation(PlanEntity plan,
            List<PlanAllocationRequest> allocations) {
        if(allocations == null || allocations.isEmpty()) {
            return Collections.emptyList();
        }

        // Implementation for saving planned allocations
        List<PlanAllocationResponse> savedAllocations = new ArrayList<>();

        for (PlanAllocationRequest allocation : allocations) {
            PlanAllocationResponse allocationResponse = new PlanAllocationResponse();
            allocationResponse.setPlanId(plan.getId());
            allocationResponse.setName(allocation.getName());
            allocationResponse.setAmountPaise(allocation.getAmountPaise());
            allocationResponse.setKind(allocation.getKind());
            allocationResponse.setDueDay(allocation.getDueDay());
            savedAllocations.add(allocationResponse);
        }

        for (PlanAllocationResponse planAllocationResponse : savedAllocations) {
            planAllocationRepository.save(planAllocationMapper.toEntity(planAllocationResponse, plan , null));
        }

        return savedAllocations;
    }

    public PlanResponse getPlan(Long userId) {
        PlanEntity planEntity = planRepository.findByProfileIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't have active plan"));

        List<PlanAllocationResponse> allocations = planAllocationMapper.toResponse(planAllocationRepository.findByPlanId(planEntity.getId()));
        CyclesEntity cyclesEntity = cycleRepository.findFirstByPlanIdOrderByStartDateDesc(planEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("No cycle found for the given plan"));

        return PlanResponse.builder()
                //Plan Details
                .id(planEntity.getId())
                .userId(userId)
                .currency(planEntity.getCurrency())
                .salaryDay(planEntity.getSalaryDay())
                .takeHomePaise(planEntity.getTakeHomePaise())
                .payFrequency(planEntity.getPayFrequencyEnum())
                .theme(planEntity.getTheme())
                .motion(planEntity.getMotion())
                .tourSeen(planEntity.getTourSeen())
                .isActive(planEntity.getActive())
                //Allocations
                .allocations(allocations)
                //Current Cycle
                .currencyCycleId(cyclesEntity.getId())
                .startDate(cyclesEntity.getStartDate())
                .endDate(cyclesEntity.getEndDate())
                .spendablePoolPaise(planEntity.getTakeHomePaise().subtract(getAllocationAmount(allocations)))
                .build();

    }
}
