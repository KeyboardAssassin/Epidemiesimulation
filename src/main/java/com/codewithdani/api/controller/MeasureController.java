package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/startvaccination")
    public void startVaccination(){
        simulationService.startVaccination();
    }

    @GetMapping("/startmedication")
    public void startMedication(){
        simulationService.startMedication();
    }

    @GetMapping("/activatecontactrestrictions")
    public void activateContactRestrictions(@RequestParam(value = "type") String type, @RequestParam(value = "name") String name, @RequestParam(value = "amountofdays") int amountOfDays){
        simulationService.activateContactRestrictions(type, name, amountOfDays);
    }

    @GetMapping("/activatesocialdistancing")
    public void activateContactRestrictions(){
        simulationService.activateSocialDistancing();
    }
}
