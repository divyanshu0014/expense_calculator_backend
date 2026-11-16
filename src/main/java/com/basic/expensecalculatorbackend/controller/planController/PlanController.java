package com.basic.expensecalculatorbackend.controller.planController;

import com.basic.expensecalculatorbackend.data.dtos.plan.*;
import com.basic.expensecalculatorbackend.services.PlanServices.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping(value = "/{userId}/plan")
    public ResponseEntity<PlanResponse> createPlan(
            @PathVariable Long userId,
            @Valid @RequestBody CreatePlanRequest createPlanRequest) {
        return new ResponseEntity<PlanResponse>(
                planService.createPlan(userId, createPlanRequest),
                HttpStatus.CREATED);
    }


    @GetMapping(value = "/{userId}/plan/active")
    public ResponseEntity<PlanResponse> getActivePlan(
            @PathVariable Long userId) {
        return new ResponseEntity<PlanResponse>(
                planService.getPlan(userId),
                HttpStatus.OK);
    }

    @PutMapping(value = "/api/v1/plans/{planId}")
    public ResponseEntity<PlanResponse> UpdatePlan(@PathVariable Long planId,
                                                @Valid @RequestBody UpdatePlanRequest updatePlanRequest) {
        return new ResponseEntity<>(
                planService.updatePlan(planId, updatePlanRequest),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "{userId}/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long userId, @PathVariable Long planId) {
        planService.deletePlan(userId, planId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //Allocations API's
    @PostMapping(value = "/api/v1/{planId}/allocations")
    public ResponseEntity<PlanAllocationResponse> createPlanAllocation(@PathVariable Long planId,
                                                                       @Valid @RequestBody PlanAllocationRequest createAllocationRequest) {
        return new ResponseEntity<PlanAllocationResponse>(
                planService.addPlanAllocation(planId, createAllocationRequest),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/v1/allocations/{allocationId}")
    public ResponseEntity<PlanAllocationResponse> updatePlanAllocations(@PathVariable Long allocationId,
                                                                        @Valid @RequestBody PlanAllocationRequest updateAllocationsRequest) {
        return new ResponseEntity<>(
                planService.updatePlanAllocations(allocationId, updateAllocationsRequest),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/v1/allocations/{allocationId}")
    public ResponseEntity<Void> deletePlanAllocations(@PathVariable Long allocationId) {
        planService.deletePlanAllocations(allocationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
