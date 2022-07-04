package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import com.codewithdani.models.summaries.CountrySummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    private final SimulationService simulationService;

    public CountryController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/getincidenceofeverystate")
    public String getIncidenceOfEveryState(){
        return simulationService.getIncidenceOfEveryState();
    }

    @GetMapping("/getincidenceofeverycity")
    public String getIncidenceOfEveryCity(){
        return simulationService.getIncidenceOfEveryCity();
    }

    @GetMapping("/getcountrysummary")
    public String getCountrySummary(){
        return simulationService.getCountrySummary();
    }

}
