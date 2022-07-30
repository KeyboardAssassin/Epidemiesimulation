package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    private final SimulationService simulationService;

    public CountryController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/getincidenceofeverystate")
    public String getIncidenceOfEveryState(@RequestParam(value = "uuid") String uuid){
        return simulationService.getIncidenceOfEveryState(uuid);
    }

    @GetMapping("/getincidenceofeverycity")
    public String getIncidenceOfEveryCity(@RequestParam(value = "uuid") String uuid){
        return simulationService.getIncidenceOfEveryCity(uuid);
    }

    @GetMapping("/getcountrysummary")
    public String getCountrySummary(@RequestParam(value = "uuid") String uuid){
        return simulationService.getCountrySummary(uuid);
    }

}
