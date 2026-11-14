package com.basic.expensecalculatorbackend.controller.planController;

import com.basic.expensecalculatorbackend.data.dtos.plan.CreatePlanRequest;
import com.basic.expensecalculatorbackend.data.dtos.plan.PlanResponse;
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
                planService.createPlan(userId, createPlanRequest), HttpStatus.CREATED);
    }


    @GetMapping(value = "/{userId}/plan/active")
    public ResponseEntity<PlanResponse> getActivePlan(
            @PathVariable Long userId) {
        return new ResponseEntity<PlanResponse>(
                planService.getPlan(userId), HttpStatus.OK);
    }

}
