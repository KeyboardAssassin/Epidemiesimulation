package com.codewithdani.api.controller;

import com.codewithdani.api.models.SimulationListResponseTO;
import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping()
    public String startSimulation(@RequestParam(value= "amountofsimulations", defaultValue = "1") int amountOfSimulations) throws IOException {
        return simulationService.startSimulation(amountOfSimulations);
    }

    @GetMapping()
    public List<SimulationListResponseTO> getAllSimulations() {
        return simulationService.getAllSimulations();
    }

    @GetMapping("/{uuid}/speed")
    public void changeSpeed(@RequestParam(value = "speed", defaultValue = "1000") int speed, @PathVariable(value = "uuid") String uuid){
        simulationService.changeSpeed(speed, uuid);
    }

    @GetMapping("/{uuid}/currentday")
    public int getCurrentDay(@PathVariable(value = "uuid") String uuid){
        return simulationService.getCurrentDay(uuid);
    }

    @GetMapping("/{uuid}/pause")
    public void pauseSimulation(@RequestParam(name = "pause") boolean pause, @PathVariable(value = "uuid") String uuid){
        simulationService.pauseSimulation(pause, uuid);
    }

    @DeleteMapping("/{uuid}")
    public void stopSimulation(@PathVariable(value = "uuid") String uuid){
        simulationService.stopSimulation(uuid);
    }
}
