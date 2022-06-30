package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StateController {

    private final SimulationService simulationService;

    public StateController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/getincidencebystate")
    public String getIncidenceByState(@RequestParam(name="statename") String name){
        return simulationService.getIncidenceByState(name);
    }
}
