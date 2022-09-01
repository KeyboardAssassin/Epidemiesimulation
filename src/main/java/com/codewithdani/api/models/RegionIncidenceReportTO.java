package com.codewithdani.api.models;

import com.codewithdani.util.SimulationUtils;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.State;

public class RegionIncidenceReportTO {
    private final String name;
    private final String incidence;

    public RegionIncidenceReportTO(City city) {
        this.name = city.getName();
        this.incidence = SimulationUtils.convertToStringWith2Digits(city.getInfectionData().getSevenDaysIncidence());
    }
    public RegionIncidenceReportTO(State state) {
        this.name = state.getName();
        this.incidence = SimulationUtils.convertToStringWith2Digits(state.getSevenDaysIncidence());
    }

    public String getName() {
        return name;
    }

    public String getIncidence() {
        return incidence;
    }
}
