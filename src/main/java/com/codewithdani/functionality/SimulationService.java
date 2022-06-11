package com.codewithdani.functionality;

import com.codewithdani.models.summaries.CitySummary;
import com.codewithdani.models.summaries.StateListSummary;
import com.codewithdani.models.summaries.StateSummary;

public interface SimulationService {

    void startSimulation(int amountOfSimulations);

    /**
     *
     * @param cityName
     * @return
     */
    CitySummary getSummary(String cityName);

    String getIncidenceByState(String stateName);

    double getIncidence(String cityName);

    void changeSpeed(int speed);

    int getDay();

    StateListSummary getIncidenceOfEveryState();
}
