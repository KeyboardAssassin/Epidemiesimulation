package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/startsimulation")
    public String startSimulation(@RequestParam(value= "amountofsimulations", defaultValue = "1") int amountOfSimulations){
        return simulationService.startSimulation(amountOfSimulations);
    }

    @GetMapping("/getallsimulations")
    public Map<String, String> getAllSimulations() {
        return simulationService.getAllSimulations();
    }

    @GetMapping("/changespeed")
    public void changeSpeed(@RequestParam(value = "speed", defaultValue = "1000") int speed, @RequestParam(value = "uuid") String uuid){
        simulationService.changeSpeed(speed, uuid);
    }

    @GetMapping("/getcurrentday")
    public int getCurrentDay(@RequestParam(value = "uuid") String uuid){
        return simulationService.getCurrentDay(uuid);
    }

    @GetMapping("/pausesimulation")
    public void pauseSimulation(@RequestParam(name = "pause") boolean pause, @RequestParam(value = "uuid") String uuid){
        simulationService.pauseSimulation(pause, uuid);
    }

    @GetMapping("/endsimulation")
    public void stopSimulation(@RequestParam(value = "uuid") String uuid){
        simulationService.stopSimulation(uuid);
    }
}
