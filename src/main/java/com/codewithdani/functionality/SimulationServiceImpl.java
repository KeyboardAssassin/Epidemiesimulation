package com.codewithdani.functionality;

import com.codewithdani.models.summaries.CityListSummary;
import com.codewithdani.models.summaries.CitySummary;
import com.codewithdani.models.summaries.StateListSummary;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class SimulationServiceImpl implements SimulationService {

    public Simulation simulation = new Simulation();
    public Gson gson = new Gson();


    @Override
    public void startSimulation(int amountOfSimulations) {
        simulation.startSimulation(amountOfSimulations);
    }


    @Override
    public CitySummary getSummary(String cityName) {
        CitySummary summary = new CitySummary();

        try{
            summary.setName(simulation.getSimulatedCountry().getCityByName(cityName).getName());
            summary.setPopulation(simulation.getSimulatedCountry().getCityByName(cityName).getPopulation());
            summary.setPopulationDensity(simulation.getSimulatedCountry().getCityByName(cityName).getPopulationDensity());
            //TODO nicht nur die neusten Erstinfektionen
            summary.setInfectedPeople(simulation.getSimulatedCountry().getCityByName(cityName).getFristInfectionNewCases());
            summary.setrValue(simulation.getSimulatedCountry().getCityByName(cityName).getrValue());
            summary.setSevenDaysIncidence(simulation.getSimulatedCountry().getCityByName(cityName).getSevenDaysIncidence());

            return summary;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        // TODO Handling falls null benötigt
        return null;


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
    public String getIncidenceOfEveryState(){
        // TODO muss der Code hier weg?
        StateListSummary summary = new StateListSummary();
        summary.fillEveryState(simulation.getSimulatedCountry());
        // TODO Dataformat (2 Nachkommastellen)
        return gson.toJson(summary.getStateListElements());
    }

    @Override
    public String getIncidenceOfEveryCity(){
        CityListSummary summary = new CityListSummary();
        summary.fillEveryCity(simulation.getSimulatedCountry());
        return gson.toJson((summary.getCitiesListElements()));
    }
}
