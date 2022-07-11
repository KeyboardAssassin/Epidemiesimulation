package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasureController {
    private final SimulationService simulationService;

    public MeasureController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/startvaccinationdevelopment")
    public void startVaccinationDevelopment(){
        simulationService.startVaccinationDevelopment();
    }

    @GetMapping("/startmedicationdevelopment")
    public void startMedicationDevelopment(){
        simulationService.startMedicationDevelopment();
    }
}
