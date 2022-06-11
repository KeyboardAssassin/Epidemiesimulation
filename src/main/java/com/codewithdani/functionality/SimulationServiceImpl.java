package com.codewithdani.functionality;

import com.codewithdani.models.summaries.CitySummary;
import com.codewithdani.models.summaries.StateListSummary;
import com.codewithdani.models.summaries.StateSummary;
import org.springframework.stereotype.Service;

@Service
public class SimulationServiceImpl implements SimulationService {

    public Simulation simulation = new Simulation();


    @Override
    public void startSimulation(int amountOfSimulations) {
        simulation.startSimulation(amountOfSimulations);
    }


    @Override
    public CitySummary getSummary(String cityName) {
        CitySummary summary = new CitySummary();

        summary.setName(simulation.getSimulatedCountry().getCityByName(cityName).getName());
        summary.setPopulation(simulation.getSimulatedCountry().getCityByName(cityName).getPopulation());
        summary.setPopulationDensity(simulation.getSimulatedCountry().getCityByName(cityName).getPopulationDensity());
        //TODO nicht nur die neusten Erstinfektionen
        summary.setInfectedPeople(simulation.getSimulatedCountry().getCityByName(cityName).getFristInfectionNewCases());
        summary.setrValue(simulation.getSimulatedCountry().getCityByName(cityName).getrValue());
        summary.setSevenDaysIncidence(simulation.getSimulatedCountry().getCityByName(cityName).getSevenDaysIncidence());

        return summary;
    }

    @Override
    public String getIncidenceByState(String stateName) {
        return simulation.getSimulatedCountry().getStateByName(stateName).calculateSevenDaysIncidence();
    }

    @Override
    public double getIncidence(String cityName) {
        return simulation.getSimulatedCountry().getCityByName(cityName).getSevenDaysIncidence();
    }

    @Override
    public void changeSpeed(int speed) {
        simulation.setSleepTime(speed);
    }

    @Override
    public int getDay(){
        return simulation.getDay();
    }

    @Override
    public StateListSummary getIncidenceOfEveryState(){
        // TODO Implementierung
        return null;
    }
}
