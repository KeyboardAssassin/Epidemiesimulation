package com.codewithdani.functionality;

import com.codewithdani.models.summaries.CitySummary;

public interface SimulationService {

    void startSimulation(int amountOfSimulations);

    /**
     *
     * @param cityName - name of the requested city
     * @return summary of the requested City
     */
    CitySummary getSummary(String cityName);

    String getIncidenceByState(String stateName);

    double getIncidence(String cityName);

    void changeSpeed(int speed);

    int getDay();

    String getIncidenceOfEveryState();

    String getIncidenceOfEveryCity();
}
