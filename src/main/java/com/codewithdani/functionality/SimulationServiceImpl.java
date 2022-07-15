package com.codewithdani.functionality;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.summaries.CityListSummary;
import com.codewithdani.models.summaries.CitySummary;
import com.codewithdani.models.summaries.CountrySummary;
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
        City requestedCity = simulation.getSimulatedCountry().getCityByName(cityName);
        CitySummary summary = new CitySummary(requestedCity);

        try{
            return summary;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        // TODO Handling falls null benötigt
        return null;
    }

    @Override
    public String getIncidenceByState(String stateName) {
        return simulation.getSimulatedCountry().getStateByName(stateName).outputSevenDaysIncidenceAsString();
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
        try{
            StateListSummary summary = new StateListSummary();
            summary.fillEveryState(simulation.getSimulatedCountry());
            
            return gson.toJson(summary.getStateListElements());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String getIncidenceOfEveryCity(){
        CityListSummary summary = new CityListSummary();
        summary.fillEveryCity(simulation.getSimulatedCountry());
        return gson.toJson((summary.getCitiesListElements()));
    }

    @Override
    public String getCountrySummary(){
        simulation.getSimulatedCountry().updateData();
        return gson.toJson(new CountrySummary(simulation.getSimulatedCountry()));
    }

    @Override
    public void startVaccinationDevelopment(){
        int dayOfDevelopmentStart = simulation.getDay();
        simulation.getSimulatedCountry().getMeasure().getVaccination().startDevelopingVaccination(dayOfDevelopmentStart);
    }

    @Override
    public void startMedicationDevelopment(){
        int dayOfDevelopmentStart = simulation.getDay();
        simulation.getSimulatedCountry().getMeasure().getMedicine().startDevelopingMedicine(dayOfDevelopmentStart);
    }

    @Override
    public void activateContactRestrictions(int amountOfDays){
        for (State state : simulation.getSimulatedCountry().getStates()) {
            state.setContactRestrictions(5);
            // TODO Method to lower the obedience for every day
        }
    }

    @Override
    public void activateSocialDistancing(int amountOfDays){
        for (State state : simulation.getSimulatedCountry().getStates()) {
            state.setContactRestrictions(2);
            // TODO Method to lower the obedience for every day
        }
    }

    @Override
    public void startVaccination(){
        simulation.getSimulatedCountry().getMeasure().getVaccination().setVaccinationStarted(true);
    }

    @Override
    public void startMedication(){
        simulation.getSimulatedCountry().getMeasure().getMedicine().setMedicationStarted(true);
    }

    @Override
    public void pauseSimulation(boolean pause){
        simulation.setSimulationPause(pause);
    }
}
