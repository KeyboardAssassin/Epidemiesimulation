package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import com.codewithdani.models.summaries.CitySummary;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/api/simulation")
public class SimulationCityController {
    private final SimulationService simulationService;

    public SimulationCityController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/{uuid}/city/incidence")
    public double getIncidence(@RequestParam(name = "cityname") String name, @PathVariable(name = "uuid") String uuid){
        return simulationService.getIncidence(name, uuid);
    }

    @GetMapping("/{uuid}/city/detailed")
    public CitySummary getCitySummary(@RequestParam(name = "cityname")  String name, @PathVariable(name = "uuid") String uuid){
        return simulationService.getSummary(name, uuid);
    }

}
