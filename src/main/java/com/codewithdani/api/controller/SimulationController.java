package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import com.codewithdani.functionality.SimulationServiceImpl;
import com.codewithdani.models.summaries.CitySummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/startsimulation")
    public void startSimulation(@RequestParam(value="amountofsimulations", defaultValue = "1") int amountofsimulations){
        simulationService.startSimulation(amountofsimulations);
    }

    @GetMapping("/changespeed")
    public void changeSpeed(@RequestParam(value = "speed", defaultValue = "1000") int speed){
        simulationService.changeSpeed(speed);
    }

    @GetMapping("/getcurrentday")
    public int getCurrentDay(){
        return simulationService.getDay();
    }
}
