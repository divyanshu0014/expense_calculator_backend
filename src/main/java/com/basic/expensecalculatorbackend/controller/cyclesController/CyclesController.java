package com.basic.expensecalculatorbackend.controller.cyclesController;

import com.basic.expensecalculatorbackend.data.model.dtos.cycles.CycleCreditRequest;
import com.basic.expensecalculatorbackend.data.model.dtos.cycles.CycleHistoryResponse;
import com.basic.expensecalculatorbackend.data.model.dtos.cycles.CycleResponse;
import com.basic.expensecalculatorbackend.data.model.dtos.cycles.StartNewCyclesRequest;
import com.basic.expensecalculatorbackend.services.CyclesServices.CyclesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cycles")
public class CyclesController {

    private final CyclesService cyclesService;

    public CyclesController(CyclesService cyclesService) {
        this.cyclesService = cyclesService;
    }

    @PostMapping(value = "/{planId}/start-next")
    public ResponseEntity<CycleResponse> startNextCycle(@PathVariable Long planId,
                                                        @Valid @RequestBody StartNewCyclesRequest cyclesRequest) {
        return new ResponseEntity<CycleResponse>(
                cyclesService.startNewCycle(planId, cyclesRequest)
                , HttpStatus.CREATED
        );
    }

    @GetMapping(value = "/current/active/{planId}")
    public ResponseEntity<CycleResponse> currentActive(@PathVariable Long planId) {
        return new ResponseEntity<>(
                cyclesService.getCurrentActiveCycle(planId),
                HttpStatus.OK
        );
    }


    @GetMapping(value = "{planId}/history")
    public ResponseEntity<List<CycleHistoryResponse>> getHistory(@PathVariable Long planId) {
        return new ResponseEntity<>(
                cyclesService.getAllCycles(planId),
                HttpStatus.OK
        );
    }


    @PutMapping(value = "/{cycleId}/credit")
    public ResponseEntity<CycleResponse> credit(@PathVariable Long cycleId,
                                                @RequestBody CycleCreditRequest creditRequest) {
        return new ResponseEntity<CycleResponse>(
                cyclesService.addCredits(cycleId, creditRequest),
                HttpStatus.OK
        );
    }
}
