package com.codewithdani.functionality;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.summaries.CitySummary;
import com.codewithdani.models.summaries.CountrySummary;
import com.codewithdani.models.summaries.ListSummary;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SimulationServiceImpl implements SimulationService {
    private final SimulationRunner simulationRunner;
    // Stores a map of uuid and the simulation
    public Map<String, Simulation> simulationList = new HashMap<>();
    public Map<String, String> simulationDates = new HashMap<>();

    private Util util = new Util();
    private final Gson gson = new Gson();

    public SimulationServiceImpl(SimulationRunner simulationRunner) {
        this.simulationRunner = simulationRunner;
    }

    @Override
    public String startSimulation(int amountOfSimulations) {
        Simulation simulation = new Simulation();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        simulationList.put(simulation.getId(), simulation);
        simulationDates.put(simulation.getId(), formatter.format(calendar.getTime()));

        simulationRunner.runSimulation(simulation, amountOfSimulations);
        return simulation.getId();
    }

    @Override
    public Map<String, String> getAllSimulations() {
        return simulationDates;
    }

    private Simulation getSimulationByUuidOrError(String uuid) {
        if (StringUtils.hasText(uuid)) {
            Simulation simulation = simulationList.get(uuid);
            if (simulation != null) {
                return simulation;
            }
        }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "uuid with id " + uuid + " does not exist!");
    }

    @Override
    public CitySummary getSummary(String cityName, String uuid) {
        City requestedCity = getSimulationByUuidOrError(uuid).getSimulatedCountry().getCityByName(cityName);
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
    public String getIncidenceByState(String stateName, String uuid) {
        return util.convertIncidenceToStringWith2Digits(getSimulationByUuidOrError(uuid).getSimulatedCountry().getStateByName(stateName).getSevenDaysIncidence());
    }

    @Override
    public double getIncidence(String cityName, String uuid) {
        return getSimulationByUuidOrError(uuid).getSimulatedCountry().getCityByName(cityName).getSevenDaysIncidence();
    }

    @Override
    public void changeSpeed(int speed, String uuid) {
        getSimulationByUuidOrError(uuid).setSleepTime(speed);
    }

    public int getCurrentDay(String uuid){
        return getSimulationByUuidOrError(uuid).getCurrentDay();
    }

    @Override
    public String getIncidenceOfEveryState(String uuid){
        try{
            ListSummary summary = new ListSummary();
            summary.fillEveryState(getSimulationByUuidOrError(uuid).getSimulatedCountry());
            
            return gson.toJson(summary.getListElements());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String getIncidenceOfEveryCity(String uuid){
        ListSummary summary = new ListSummary();
        summary.fillEveryCity(getSimulationByUuidOrError(uuid).getSimulatedCountry());
        return gson.toJson((summary.getListElements()));
    }

    @Override
    public String getCountrySummary(String uuid){
        Simulation simulation = getSimulationByUuidOrError(uuid);
        simulation.getSimulatedCountry().updateData();
        return gson.toJson(new CountrySummary(simulation.getSimulatedCountry(), util));
    }

    @Override
    public void startVaccinationDevelopment(String uuid){
        Simulation simulation = getSimulationByUuidOrError(uuid);
        int dayOfDevelopmentStart = simulation.getCurrentDay();
        simulation.getSimulatedCountry().getMeasure().getVaccination().startDevelopingVaccination(dayOfDevelopmentStart);
    }

    @Override
    public void startMedicationDevelopment(String uuid){
        Simulation simulation = getSimulationByUuidOrError(uuid);
        int dayOfDevelopmentStart = simulation.getCurrentDay();
        simulation.getSimulatedCountry().getMeasure().getMedicine().startDevelopingMedicine(dayOfDevelopmentStart);
    }

    @Override
    public void activateContactRestrictions(String type, String name, int amountOfDays, String uuid){
        Simulation simulation = getSimulationByUuidOrError(uuid);
        int contactRestrictionValue = 5;

        switch (type) {
            case "country":
                for (State state : simulation.getSimulatedCountry().getStates()) {
                    state.setContactRestrictions(contactRestrictionValue);
                    state.setContactRestrictionDuration(amountOfDays);
                    state.updateAllCitiesContactRestrictions(contactRestrictionValue);
                    break;
                }
            case "state":
                State state = simulation.getSimulatedCountry().getStateByName(name);

                state.setContactRestrictions(contactRestrictionValue);
                state.setContactRestrictionDuration(amountOfDays);
                state.updateAllCitiesContactRestrictions(contactRestrictionValue);
                break;
            case "city":
                City city = simulation.getSimulatedCountry().getCityByName(name);

                city.setContactRestrictionsOfMotherState(contactRestrictionValue);
                city.setContactRestrictionDuration(amountOfDays);
                break;
        }
    }

    @Override
    public void activateSocialDistancing(String uuid){
        getSimulationByUuidOrError(uuid).getSimulatedCountry().setSocialDistancingActivated(true);
    }

    @Override
    public void startVaccination(String uuid){
        getSimulationByUuidOrError(uuid).getSimulatedCountry().getMeasure().getVaccination().setVaccinationStarted(true);
    }

    @Override
    public void startMedication(String uuid){
        getSimulationByUuidOrError(uuid).getSimulatedCountry().getMeasure().getMedicine().setMedicationStarted(true);
    }

    @Override
    public void pauseSimulation(boolean pause, String uuid){
        getSimulationByUuidOrError(uuid).setSimulationPause(pause);
    }

    @Override
    public void stopSimulation(String uuid){
        getSimulationByUuidOrError(uuid).setStopSimulation(true);
        simulationList.remove(uuid);
    }
}
