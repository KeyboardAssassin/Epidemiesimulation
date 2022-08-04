package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/simulation")
public class SimulationStateController {

    private final SimulationService simulationService;

    public SimulationStateController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/{uuid}/state/incidence")
    public String getIncidenceByState(@RequestParam(name="statename") String name, @PathVariable(name="uuid") String uuid){
        return simulationService.getIncidenceByState(name, uuid);
    }
}
