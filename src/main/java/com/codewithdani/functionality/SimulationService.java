package com.codewithdani.functionality;

import com.codewithdani.models.summaries.CitySummary;
import com.codewithdani.models.summaries.CountrySummary;

public interface SimulationService {

    /**
     * Method to start the simulation
     *
     * @param amountOfSimulations - how many simulations should run behind each other
     */
    void startSimulation(int amountOfSimulations);

    /**
     * Method to start the internal vaccination development
     */
    void startVaccinationDevelopment();

    /**
     * Method to start the vaccination campaign
     */
    void startVaccination();

    /**
     * Method to start the usage of medication in the hospitals
     */
    void startMedication();

    /**
     * Method to start the internal medication development
     */
    void startMedicationDevelopment();

    /**
     * Method to activate a contact restriction measure
     *
     * @param amountOfDays - amount of Days the contact restriction should last
     * @param type - country, state or city
     * @param amountOfDays - How many days the restriction should last
     */
    void activateContactRestrictions(String type, String name, int amountOfDays);

    /**
     * Method to activate a social distancing measure
     */
    void activateSocialDistancing();

    /**
     * Method to create a CitySummary of a certain city
     *
     * @param cityName - name of the requested city
     * @return summary of the requested City
     */
    CitySummary getSummary(String cityName);

    /**
     * Method to get the incidence of a state
     *
     * @param stateName - name of the requested state
     * @return incidence (member variable) of the state
     */
    String getIncidenceByState(String stateName);

    /**
     * Method to get the incidence of a city
     *
     * @param cityName - name of the requested city
     * @return incidence (member variable) of the city
     */
    double getIncidence(String cityName);

    /**
     * Method to change the speed of the simulation (how fast new days begin)
     *
     * @param speed - speed value (the greater the value the longer the waiting time between days)
     */
    void changeSpeed(int speed);

    /**
     * Method to return the current day of the simulation (e.g. 10)
     *
     * @return - current day
     */
    int getDay();

    /**
     * Method to return all incidences of every state
     *
     * @return returns json object with format ({state, incidence}) of every state
     */
    String getIncidenceOfEveryState();

    /**
     * Method to return all incidences of every city of every state
     *
     * @return returns json object with format ({city, incidence}) of every state and every city
     */
    String getIncidenceOfEveryCity();

    /**
     * Method to return a country summary including incidence, rValue, newInfections and deathCases
     *
     * @return return json object with format {incidence, rValue, newInfections, deathCases}
     */
    String getCountrySummary();

    /**
     * Method that can be invoked to pause the simulation
     *
     * @param pause - true = pause - false = run
     */
    void pauseSimulation(boolean pause);
}
