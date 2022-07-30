package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import com.codewithdani.models.summaries.CitySummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class CityController {
    private final SimulationService simulationService;

    public CityController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/getincidence")
    public double getIncidence(@RequestParam(name = "cityname") String name, @RequestParam(name = "uuid") String uuid){
        return simulationService.getIncidence(name, uuid);
    }

    @GetMapping("/getdetailed")
    public CitySummary getCitySummary(@RequestParam(name = "cityname")  String name, @RequestParam(name = "uuid") String uuid){
        return simulationService.getSummary(name, uuid);
    }

}
