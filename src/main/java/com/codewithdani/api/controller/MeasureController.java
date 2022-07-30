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
    public void startVaccinationDevelopment(@RequestParam(value = "uuid") String uuid){
        simulationService.startVaccinationDevelopment(uuid);
    }

    @GetMapping("/startmedicationdevelopment")
    public void startMedicationDevelopment(@RequestParam(value = "uuid") String uuid){
        simulationService.startMedicationDevelopment(uuid);
    }

    @GetMapping("/startvaccination")
    public void startVaccination(@RequestParam(value = "uuid") String uuid){
        simulationService.startVaccination(uuid);
    }

    @GetMapping("/startmedication")
    public void startMedication(@RequestParam(value = "uuid") String uuid){
        simulationService.startMedication(uuid);
    }

    @GetMapping("/activatecontactrestrictions")
    public void activateContactRestrictions(@RequestParam(value = "type") String type, @RequestParam(value = "name") String name, @RequestParam(value = "amountofdays") int amountOfDays, @RequestParam(value = "uuid") String uuid){
        simulationService.activateContactRestrictions(type, name, amountOfDays, uuid);
    }

    @GetMapping("/activatesocialdistancing")
    public void activateContactRestrictions(@RequestParam(value = "uuid") String uuid){
        simulationService.activateSocialDistancing(uuid);
    }
}
