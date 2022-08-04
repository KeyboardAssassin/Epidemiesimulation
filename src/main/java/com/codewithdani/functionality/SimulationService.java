package com.codewithdani.functionality;

import com.codewithdani.api.models.CountrySummaryTO;
import com.codewithdani.api.models.SimulationListResponseTO;
import com.codewithdani.api.models.CitySummaryTO;
import com.codewithdani.api.models.RegionIncidenceReportTO;

import java.util.List;

public interface SimulationService {

    /**
     * Method to start the simulation
     *
     * @param amountOfSimulations - how many simulations should run behind each other
     * @return uuid of the newly created simulation
     */
    String startSimulation(int amountOfSimulations);


    /**
     * Method to get all Simulationsids with timestamp of creation
     *
     * @return list with uuid + timestamp
     */
    List<SimulationListResponseTO> getAllSimulations();


    /**
     * Method to start the internal vaccination development
     */
    void startVaccinationDevelopment(String uuid);

    /**
     * Method to start the vaccination campaign
     */
    void startVaccination(String uuid);

    /**
     * Method to start the usage of medication in the hospitals
     */
    void startMedication(String uuid);

    /**
     * Method to start the internal medication development
     */
    void startMedicationDevelopment(String uuid);

    /**
     * Method to activate a contact restriction measure
     *
     * @param type         - type on which level the restrictions should be activated "country" - "state" - "city"
     * @param name         - name of the state or city
     * @param amountOfDays - How many days the restriction should last
     * @param uuid
     */
    void activateContactRestrictions(String type, String name, int amountOfDays, String uuid);

    /**
     * Method to activate a social distancing measure
     */
    void activateSocialDistancing(String uuid);

    /**
     * Method to create a CitySummary of a certain city
     *
     * @param cityName - name of the requested city
     * @param simulationUuid - uuid of the simulation
     * @return summary of the requested City
     */
    CitySummaryTO getSummary(String cityName, String simulationUuid);

    /**
     * Method to get the incidence of a state
     *
     * @param stateName - name of the requested state
     * @param uuid
     * @return incidence (member variable) of the state
     */
    String getIncidenceByState(String stateName, String uuid);

    /**
     * Method to get the incidence of a city
     *
     * @param cityName - name of the requested city
     * @param uuid
     * @return incidence (member variable) of the city
     */
    double getIncidence(String cityName, String uuid);

    /**
     * Method to change the speed of the simulation (how fast new days begin)
     *
     * @param speed - speed value (the greater the value the longer the waiting time between days)
     * @param uuid
     */
    void changeSpeed(int speed, String uuid);

    /**
     * Method to return the current day of the simulation (e.g. 10)
     *
     * @return - current day
     */
    int getCurrentDay(String uuid);

    /**
     * Method to return all incidences of every state
     *
     * @return returns json object with format ({state, incidence}) of every state
     */
    List<RegionIncidenceReportTO> getIncidenceOfEveryState(String uuid);

    /**
     * Method to return all incidences of every city of every state
     *
     * @return returns json object with format ({city, incidence}) of every state and every city
     */
    List<RegionIncidenceReportTO> getIncidenceOfEveryCity(String uuid);

    /**
     * Method to return a country summary including incidence, rValue, newInfections and deathCases
     *
     * @return return json object with format {incidence, rValue, newInfections, deathCases}
     */
    CountrySummaryTO getCountrySummary(String uuid);

    /**
     * Method that can be invoked to pause the simulation
     *
     * @param pause - true = pause - false = run
     * @param uuid-  the ID of the simulation to pause
     */
    void pauseSimulation(boolean pause, String uuid);

    /**
     * Method that can be invoked to terminate the simulation
     *
     * @param uuid - the ID of the simulation to end
     */
    void stopSimulation(String uuid);

    /**
     * Method to get the current obedience of a nation
     *
     * @return obedience between 0 (no obedience) and 1 (obedience)
     */
    double getObedience(String uuid);
}
