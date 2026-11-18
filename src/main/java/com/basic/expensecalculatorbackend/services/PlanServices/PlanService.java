package com.basic.expensecalculatorbackend.services.PlanServices;

import com.basic.expensecalculatorbackend.data.model.entities.CyclesEntity;
import com.basic.expensecalculatorbackend.data.model.entities.PlanAllocationEntity;
import com.basic.expensecalculatorbackend.data.model.entities.PlanEntity;
import com.basic.expensecalculatorbackend.data.model.entities.UserProfileEntity;
import com.basic.expensecalculatorbackend.data.enums.SourceEnum;
import com.basic.expensecalculatorbackend.data.model.Plan;
import com.basic.expensecalculatorbackend.data.model.dtos.plan.*;
import com.basic.expensecalculatorbackend.mapper.PlanAllocationMapper;
import com.basic.expensecalculatorbackend.mapper.PlanMapper;
import com.basic.expensecalculatorbackend.repositories.CycleRepository;
import com.basic.expensecalculatorbackend.repositories.PlanAllocationRepository;
import com.basic.expensecalculatorbackend.repositories.PlanRepository;
import com.basic.expensecalculatorbackend.repositories.UserProfileRepository;

import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final UserProfileRepository userProfileRepository;
    private final PlanAllocationRepository planAllocationRepository;
    private final PlanMapper planMapper;
    private final PlanAllocationMapper planAllocationMapper;
    private final CycleRepository cycleRepository;

    public PlanService(PlanRepository planRepository, UserProfileRepository userProfileRepository, PlanAllocationRepository planAllocationRepository, PlanMapper planMapper, PlanAllocationMapper planAllocationMapper, CycleRepository cycleRepository) {
        this.planRepository = planRepository;
        this.planAllocationRepository = planAllocationRepository;
        this.planMapper = planMapper;
        this.planAllocationMapper = planAllocationMapper;
        this.userProfileRepository = userProfileRepository;
        this.cycleRepository = cycleRepository;
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
        plan.setPayFrequency(planRequest.getPayFrequency());
        plan.setIsActive(true);
        return plan;
    }

    @Transactional
    public PlanResponse createPlan(Long userId, CreatePlanRequest planRequest) {
        UserProfileEntity userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not Found"));

        Plan plan = getPlan(planRequest);

        PlanEntity savedPlan = planRepository.save(planMapper.toPlanEntity(plan, userProfile, null));
        List<PlanAllocationResponse> allocations = getSavedPlannedAllocation(savedPlan, planRequest.getAllocations());

        CyclesEntity initialCycle = createAndSaveInitialCycle(savedPlan, planRequest.getOpeningCarryPaise(), planRequest.getLabel());

        return PlanResponse.builder().id(savedPlan.getId()).userId(userId).currency(planRequest.getCurrency()).salaryDay(planRequest.getSalaryDay()).takeHomePaise(planRequest.getTakeHomePaise()).allocations(allocations).currentCycleId(initialCycle.getId()).startDate(initialCycle.getStartDate()).endDate(initialCycle.getEndDate()).spendablePoolPaise(savedPlan.getTakeHomePaise().subtract(getAllocationAmount(allocations))).isActive(plan.getIsActive()).build();
    }

    private BigDecimal getAllocationAmount(List<PlanAllocationResponse> allocations) {
        return allocations.stream().map(PlanAllocationResponse::getAmountPaise).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CyclesEntity createAndSaveInitialCycle(PlanEntity savedPlan, BigDecimal openingCarryPaise, String label) {
        CyclesEntity initialCycle = new CyclesEntity();
        initialCycle.setPlan(savedPlan);
        initialCycle.setSource(SourceEnum.APP);
        initialCycle.setTakeHomePaise(savedPlan.getTakeHomePaise());

        initialCycle.setExtraCreditsPaise(BigDecimal.ZERO);
        initialCycle.setOpeningCarryPaise(Objects.nonNull(openingCarryPaise) ? openingCarryPaise : BigDecimal.ZERO);
        initialCycle.setReleasedCarryPaise(BigDecimal.ZERO);

        LocalDate today = LocalDate.now();
        int salaryDay = savedPlan.getSalaryDay().intValue();

        LocalDate cycleStart;
        LocalDate cycleEnd;

        if (today.getDayOfMonth() >= salaryDay) {
            cycleStart = clampDate(today.getYear(), today.getMonthValue(), salaryDay);
            LocalDate nextMonth = today.plusMonths(1);
            cycleEnd = clampDate(nextMonth.getYear(), nextMonth.getMonthValue(), salaryDay);
        } else {
            LocalDate prevMonth = today.minusMonths(1);
            cycleStart = clampDate(prevMonth.getYear(), prevMonth.getMonthValue(), salaryDay);
            cycleEnd = clampDate(today.getYear(), today.getMonthValue(), salaryDay);
        }

        if (label == null || label.isBlank()) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM");
            // e.g. "25 Oct – 24 Nov" (end date minus 1 day is the last day of the cycle)
            label = cycleStart.format(fmt) + " – " + cycleEnd.minusDays(1).format(fmt);
        }
        initialCycle.setLabel(label);

        initialCycle.setStartDate(cycleStart);
        initialCycle.setEndDate(cycleEnd);

        return cycleRepository.save(initialCycle);

    }

    private LocalDate clampDate(int year, int month, int targetDay) {
        int maxDays = YearMonth.of(year, month).lengthOfMonth();
        return LocalDate.of(year, month, Math.min(targetDay, maxDays));
    }


    private List<PlanAllocationResponse> getSavedPlannedAllocation(PlanEntity plan, List<PlanAllocationRequest> allocations) {
        if (allocations == null || allocations.isEmpty()) {
            return Collections.emptyList();
        }

        // Implementation for saving planned allocations
        List<PlanAllocationResponse> savedAllocations = new ArrayList<>();

        for (PlanAllocationRequest allocation : allocations) {
            PlanAllocationEntity entity = new PlanAllocationEntity();
            entity.setPlan(plan);
            entity.setName(allocation.getName());
            entity.setAmountPaise(allocation.getAmountPaise());
            entity.setKind(allocation.getKind());
            entity.setDueDay(allocation.getDueDay());

            PlanAllocationEntity saved = planAllocationRepository.save(entity);
            savedAllocations.add(planAllocationMapper.toResponse(saved));
        }


        return savedAllocations;
    }

    public PlanResponse getPlan(Long planId) {
        PlanEntity planEntity = planRepository.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        List<PlanAllocationResponse> allocations = planAllocationMapper.toResponse(planAllocationRepository.findAllocationsByPlanId(planEntity.getId()));
        CyclesEntity cyclesEntity = cycleRepository.findFirstByPlanIdAndIsActiveTrueOrderByStartDateDesc(planEntity.getId()).orElseThrow(() -> new IllegalArgumentException("No cycle found for the given plan"));

        return PlanResponse.builder()
                //Plan Details
                .id(planEntity.getId()).userId(planEntity.getProfile().getId()).currency(planEntity.getCurrency()).salaryDay(planEntity.getSalaryDay()).takeHomePaise(planEntity.getTakeHomePaise()).payFrequency(planEntity.getPayFrequencyEnum()).theme(planEntity.getTheme()).motion(planEntity.getMotion()).tourSeen(planEntity.getTourSeen()).isActive(planEntity.getActive())
                //Allocations
                .allocations(allocations)
                //Current Cycle
                .currentCycleId(cyclesEntity.getId()).startDate(cyclesEntity.getStartDate()).endDate(cyclesEntity.getEndDate()).spendablePoolPaise(planEntity.getTakeHomePaise().subtract(getAllocationAmount(allocations))).build();

    }

    @Transactional
    public PlanResponse updatePlan(Long planId, @Valid UpdatePlanRequest request) {
        //PlaceHolder for Adding Plan Logic
        PlanEntity plan = planRepository.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        //Update Plan
        plan.setCurrency(request.getCurrency() != null ? request.getCurrency() : plan.getCurrency());
        plan.setSalaryDay(request.getSalaryDay() != null ? request.getSalaryDay() : plan.getSalaryDay());
        plan.setTakeHomePaise(request.getTakeHomePaise() != null ? request.getTakeHomePaise() : plan.getTakeHomePaise());
        plan.setPayFrequencyEnum(request.getPayFrequency() != null ? request.getPayFrequency() : plan.getPayFrequencyEnum());
        plan.setTheme(request.getTheme() != null ? request.getTheme() : plan.getTheme());
        plan.setMotion(request.getMotion() != null ? request.getMotion() : plan.getMotion());
        plan.setTourSeen(request.getTourSeen() != null ? request.getTourSeen() : plan.getTourSeen());

        if (Objects.isNull(plan.getReleasedCarryPaise())) {
            plan.setReleasedCarryPaise(request.getReleasedCarryPaise());
        }

        PlanEntity updatedPlan = planRepository.save(plan);

        cycleRepository.findFirstByPlanIdAndIsActiveTrueOrderByStartDateDesc(updatedPlan.getId()).ifPresent(currentCycle -> {
            if (Objects.nonNull(request.getTakeHomePaise())) {
                currentCycle.setTakeHomePaise(request.getTakeHomePaise());
            }
            if (Objects.nonNull(currentCycle.getReleasedCarryPaise())) {
                currentCycle.setReleasedCarryPaise(currentCycle.getReleasedCarryPaise());
            }

            cycleRepository.save(currentCycle);
        });
        return getPlan(updatedPlan.getId());
    }

    @Transactional
    public void deletePlan(Long userId, Long planId) {
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        if (!userId.equals(plan.getProfile().getId())) {
            throw new IllegalArgumentException("PlanId" + planId + " does not belong to user");
        }

        plan.setActive(false);
        planRepository.save(plan);
    }

    //Allocations Methods
    @Transactional
    public PlanAllocationResponse addPlanAllocation(Long planId, PlanAllocationRequest request) {
       PlanEntity plan = planRepository.findById(planId)
               .orElseThrow(() -> new IllegalArgumentException("Plan with ID " + planId + " does not exist"));

       PlanAllocationEntity newPlanAllocation = new PlanAllocationEntity();
       newPlanAllocation.setPlan(plan);
       newPlanAllocation.setName(request.getName());
       newPlanAllocation.setAmountPaise(request.getAmountPaise());
       newPlanAllocation.setKind(request.getKind());
       newPlanAllocation.setDueDay(request.getDueDay());

       PlanAllocationEntity savedAllocation = planAllocationRepository.save(newPlanAllocation);

       return planAllocationMapper.toResponse(List.of(savedAllocation)).get(0);
    }



    @Transactional
    public PlanAllocationResponse updatePlanAllocations(Long allocationId, @Valid PlanAllocationRequest updateAllocationsRequest) {
        //PlaceHolder for Adding Plan Allocation Logic
        PlanAllocationEntity existingAllocation = planAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new IllegalArgumentException("AllocationId" + allocationId + " does not exist"));

        existingAllocation.setAmountPaise(updateAllocationsRequest.getAmountPaise());
        existingAllocation.setName(updateAllocationsRequest.getName());
        existingAllocation.setKind(updateAllocationsRequest.getKind());
        existingAllocation.setDueDay(updateAllocationsRequest.getDueDay());

        PlanAllocationEntity updated = planAllocationRepository.save(existingAllocation);
        return planAllocationMapper.toResponse(updated);
    }

    @Transactional
    public void deletePlanAllocations(Long allocationId) {
        if(!planAllocationRepository.findById(allocationId).isPresent()) {
            throw new IllegalArgumentException("Allocation with ID " + allocationId + " does not exist");
        }

        planAllocationRepository.deleteById(allocationId);
    }


}
