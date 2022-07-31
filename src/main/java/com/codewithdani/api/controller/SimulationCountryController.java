package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/simulation")
public class SimulationCountryController {

    private final SimulationService simulationService;

    public SimulationCountryController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/{uuid}/country/incidenceofeverystate")
    public String getIncidenceOfEveryState(@PathVariable(value = "uuid") String uuid){
        return simulationService.getIncidenceOfEveryState(uuid);
    }

    @GetMapping("/{uuid}/country/incidenceofeverycity")
    public String getIncidenceOfEveryCity(@PathVariable(value = "uuid") String uuid){
        return simulationService.getIncidenceOfEveryCity(uuid);
    }

    @GetMapping("/{uuid}/country/countrysummary")
    public String getCountrySummary(@PathVariable(value = "uuid") String uuid){
        return simulationService.getCountrySummary(uuid);
    }

}
