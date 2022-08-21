package com.codewithdani.api.controller;

import com.codewithdani.api.models.CountrySummaryTO;
import com.codewithdani.functionality.SimulationService;
import com.codewithdani.api.models.RegionIncidenceReportTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/simulation")
public class SimulationCountryController {

    private final SimulationService simulationService;

    public SimulationCountryController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/{uuid}/country/incidenceofeverystate")
    public List<RegionIncidenceReportTO> getIncidenceOfEveryState(@PathVariable(value = "uuid") String uuid){
        return simulationService.getIncidenceOfEveryState(uuid);
    }

    @GetMapping("/{uuid}/country/incidenceofeverycity")
    public List<RegionIncidenceReportTO> getIncidenceOfEveryCity(@PathVariable(value = "uuid") String uuid){
        return simulationService.getIncidenceOfEveryCity(uuid);
    }

    @GetMapping("/{uuid}/country/summary")
    public CountrySummaryTO getCountrySummary(@PathVariable(value = "uuid") String uuid){
        return simulationService.getCountrySummary(uuid);
    }

    @GetMapping("/{uuid}/country/obedience")
    public double getObedience(@PathVariable(value = "uuid") String uuid){
        return simulationService.getObedience(uuid);
    }
}
