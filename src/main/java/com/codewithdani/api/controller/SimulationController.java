package com.codewithdani.api.controller;

import com.codewithdani.functionality.Simulation;
import com.codewithdani.models.summaries.CitySummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulationController {
    Simulation simulation = new Simulation();

    @GetMapping("/startsimulation")
    public void startSimulation(){
        simulation.startSimulation();
    }

    @GetMapping("/changespeed")
    public void changeSpeed(@RequestParam(value = "speed", defaultValue = "1000") int speed){
        simulation.setSleepTime(speed);
    }


    // sollte eigentlich in den CityController aber dort gibt es das simulation Objekt nicht..?
    @GetMapping("/getincidence")
    public double getIncidence(@RequestParam(name = "cityname") String name){
        return simulation.getSimulatedCountry().getCityByName(name).getSevenDaysIncidence();
    }

    @GetMapping("/getdetailed")
    public CitySummary getSummary(@RequestParam(name = "cityname") String name){
        CitySummary summary = new CitySummary();

        summary.setName(simulation.getSimulatedCountry().getCityByName(name).getName());
        summary.setPopulation(simulation.getSimulatedCountry().getCityByName(name).getPopulation());
        summary.setPopulationDensity(simulation.getSimulatedCountry().getCityByName(name).getPopulationDensity());
        //TODO nicht nur die neusten Erstinfektionen
        summary.setInfectedPeople(simulation.getSimulatedCountry().getCityByName(name).getFristInfectionNewCases());
        summary.setrValue(simulation.getSimulatedCountry().getCityByName(name).getrValue());
        summary.setSevenDaysIncidence(simulation.getSimulatedCountry().getCityByName(name).getSevenDaysIncidence());

        return summary;
    }


}
