package com.codewithdani.models.summaries;

import com.codewithdani.functionality.Util;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;

import java.util.ArrayList;

public class CityListSummary {
    private ArrayList<CityListElement> citiesListElements;
    private Util util = new Util();

    public CityListSummary() {
        this.citiesListElements = new ArrayList<>();
    }

    public void fillEveryCity(Country country){
        for (State state: country.getStates()) {
            for (City city: state.getCities()) {
                CityListElement element = new CityListElement(city, util);
                citiesListElements.add(element);
            }
        }
    }

    public ArrayList<CityListElement> getCitiesListElements() {
        return citiesListElements;
    }
}
