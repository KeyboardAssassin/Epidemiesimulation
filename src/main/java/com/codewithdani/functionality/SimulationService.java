package com.codewithdani.functionality;

import com.codewithdani.models.summaries.CitySummary;

public interface SimulationService {

    void startSimulation(int amountOfSimulations);

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
}
