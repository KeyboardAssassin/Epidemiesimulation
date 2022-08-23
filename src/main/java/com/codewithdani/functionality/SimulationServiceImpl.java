package com.codewithdani.functionality;

import com.codewithdani.api.models.SimulationListResponseTO;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.State;
import com.codewithdani.api.models.CitySummaryTO;
import com.codewithdani.api.models.CountrySummaryTO;
import com.codewithdani.api.models.RegionIncidenceReportTO;
import com.codewithdani.util.SimulationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SimulationServiceImpl implements SimulationService {
    private final SimulationRunner simulationRunner;
    // Stores a map of uuid and the simulation
    public Map<String, Simulation> simulationList = new HashMap<>();

    public SimulationServiceImpl(SimulationRunner simulationRunner) {
        this.simulationRunner = simulationRunner;
    }

    @Override
    public String startSimulation(int amountOfSimulations) throws IOException {
        Simulation simulation = new Simulation();
        simulationList.put(simulation.getId(), simulation);
        simulationRunner.runSimulation(simulation, amountOfSimulations);
        return simulation.getId();
    }

    @Override
    public List<SimulationListResponseTO> getAllSimulations() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        return simulationList.values().stream()
                .map(simulation -> new SimulationListResponseTO(simulation.getId(),formatter.format(simulation.getCreationDate())))
                .sorted(Comparator.comparing(SimulationListResponseTO::getDate))
                .collect(Collectors.toList());
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
    public CitySummaryTO getSummary(String cityName, String uuid) {
        if (StringUtils.hasText(cityName) && StringUtils.hasText(uuid)) {
            City requestedCity = getSimulationByUuidOrError(uuid).getSimulatedCountry().getCityByName(cityName);
            CitySummaryTO summary = new CitySummaryTO(requestedCity);

            if (summary != null){
                return summary;
            }
        }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "uuid with id " + uuid + " or city with name " + cityName + " does not exist!");
    }

    @Override
    public String getIncidenceByState(String stateName, String uuid) {
        return SimulationUtils.convertIncidenceToStringWith2Digits(getSimulationByUuidOrError(uuid).getSimulatedCountry().getStateByName(stateName).getSevenDaysIncidence());
    }

    @Override
    public double getIncidence(String cityName, String uuid) {
        return getSimulationByUuidOrError(uuid).getSimulatedCountry().getCityByName(cityName).getInfectionData().getSevenDaysIncidence();
    }

    @Override
    public void changeSpeed(int speed, String uuid) {
        getSimulationByUuidOrError(uuid).setSleepTime(speed);
    }

    @Override
    public int getCurrentDay(String uuid){
        return getSimulationByUuidOrError(uuid).getCurrentDay();
    }

    @Override
    public List<RegionIncidenceReportTO> getIncidenceOfEveryState(String uuid){
        try{
            return Arrays.stream(getSimulationByUuidOrError(uuid).getSimulatedCountry().getStates())
                    .map(RegionIncidenceReportTO::new)
                    .collect(Collectors.toList());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not able to provide list with incidences of simulation with uuid " + uuid);
    }

    @Override
    public List<RegionIncidenceReportTO> getIncidenceOfEveryCity(String uuid){
       return  Arrays.stream(getSimulationByUuidOrError(uuid).getSimulatedCountry().getStates())
                .flatMap(state -> state.getCities().stream())
                .map(RegionIncidenceReportTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CountrySummaryTO getCountrySummary(String uuid){
        Simulation simulation = getSimulationByUuidOrError(uuid);
        CountrySummaryTO summary = new CountrySummaryTO(simulation.getSimulatedCountry());

        if (summary != null){
            return summary;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "uuid with id " + uuid + " does not exist!");
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

        switch (type) {
            case "country":
                for (State state : simulation.getSimulatedCountry().getStates()) {
                    state.activateContactRestrictions(amountOfDays);
                }
                break;
            case "state":
                simulation.getSimulatedCountry().getStateByName(name).activateContactRestrictions(amountOfDays);
                break;
            case "city":
                simulation.getSimulatedCountry().getCityByName(name).activateContactRestrictions(amountOfDays);
                break;
        }
    }

    @Override
    public void toggleSocialDistancing(String uuid, boolean status){
        getSimulationByUuidOrError(uuid).getSimulatedCountry().setSocialDistancing(status);
    }

    @Override
    public void removeCountryRestrictions(String uuid){
        getSimulationByUuidOrError(uuid).getSimulatedCountry().resetAllRestrictions();
    }

    @Override
    public void removeStateRestrictions(String uuid, String stateName){
        getSimulationByUuidOrError(uuid).getSimulatedCountry().resetOneStateRestrictions(stateName);
    }

    @Override
    public void removeCityRestrictions(String uuid, String cityName){
        getSimulationByUuidOrError(uuid).getSimulatedCountry().resetOneCityRestrictions(cityName);
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

    @Override
    public double getObedience(String uuid){
        return getSimulationByUuidOrError(uuid).getSimulatedCountry().getObedience();
    }
}
