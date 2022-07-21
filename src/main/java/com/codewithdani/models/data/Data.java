package com.codewithdani.models.data;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;

public class Data {
    private int highestCityDensity;
    private int lowestCityDensity;

    public int getHighestCityDensity() {
        return highestCityDensity;
    }

    public int getLowestCityDensity() {
        return lowestCityDensity;
    }

    public void setHighestCityDensity(Country country) {
        int highestDensity = 0;

        for (State state: country.getStates()) {
            for (City city: state.getCities()) {
                if (city.getPopulationDensity() > highestDensity){
                    highestDensity = city.getPopulationDensity();
                }
            }
        }
        this.highestCityDensity = highestDensity;
    }

    public void setLowestCityDensity(Country country) {
        int lowestDensity = 10000;

        for (State state: country.getStates()) {
            for (City city: state.getCities()) {
                if (city.getPopulationDensity() < lowestDensity){
                    lowestDensity = city.getPopulationDensity();
                }
            }
        }
        this.lowestCityDensity = lowestDensity;
    }
}
