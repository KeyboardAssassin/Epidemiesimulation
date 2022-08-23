package com.codewithdani.api.controller;

import com.codewithdani.functionality.SimulationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/simulation")
public class SimulationMeasureController {
    private final SimulationService simulationService;

    public SimulationMeasureController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PutMapping("/{uuid}/measure/vaccinationdevelopment")
    public void startVaccinationDevelopment(@PathVariable(value = "uuid") String uuid){
        simulationService.startVaccinationDevelopment(uuid);
    }

    @PutMapping("/{uuid}/measure/medicationdevelopment")
    public void startMedicationDevelopment(@PathVariable(value = "uuid") String uuid){
        simulationService.startMedicationDevelopment(uuid);
    }

    @PutMapping("/{uuid}/measure/vaccination")
    public void startVaccination(@PathVariable(value = "uuid") String uuid){
        simulationService.startVaccination(uuid);
    }

    @PutMapping("/{uuid}/measure/medication")
    public void startMedication(@PathVariable(value = "uuid") String uuid){
        simulationService.startMedication(uuid);
    }

    @GetMapping("/{uuid}/measure/contactrestrictions")
    public void activateSocialDistancing(@RequestParam(value = "type") String type, @RequestParam(value = "name") String name, @RequestParam(value = "amountofdays") int amountOfDays, @PathVariable(value = "uuid") String uuid){
        simulationService.activateContactRestrictions(type, name, amountOfDays, uuid);
    }

    @PutMapping("/{uuid}/measure/socialdistancing")
    public void activateSocialDistancing(@PathVariable(value = "uuid") String uuid){
        simulationService.toggleSocialDistancing(uuid, true);
    }

    @DeleteMapping("/{uuid}/measure/socialdistancing")
    public void deactivateSocialDistancing(@PathVariable(value = "uuid") String uuid){
        simulationService.toggleSocialDistancing(uuid, false);
    }

    @DeleteMapping("/{uuid}/measure/countryrestrictions")
    public void deactivateCountryRestrictions(@PathVariable(value = "uuid") String uuid){
        simulationService.removeCountryRestrictions(uuid);
    }

    @DeleteMapping("/{uuid}/measure/staterestrictions")
    public void deactivateStateRestrictions(@PathVariable(value = "uuid") String uuid, @RequestParam(value = "name") String stateName){
        simulationService.removeStateRestrictions(uuid, stateName);
    }

    @DeleteMapping("/{uuid}/measure/cityrestrictions")
    public void deactivateCityRestrictions(@PathVariable(value = "uuid") String uuid, @RequestParam(value = "name") String cityName){
        simulationService.removeCityRestrictions(uuid, cityName);
    }
}
