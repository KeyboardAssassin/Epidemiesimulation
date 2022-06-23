package com.codewithdani.models.summaries;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.State;

public class CityListElement {
    private String name;
    private double incidence;

    public CityListElement(City city) {
        this.name = city.getName();
        this.incidence = city.getSevenDaysIncidence();
    }
}
