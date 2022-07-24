package com.codewithdani.models.data;

import com.codewithdani.models.regional.Country;
import java.util.Arrays;
import java.util.stream.IntStream;

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
        this.highestCityDensity = getPopulationDensityStream(country).max().getAsInt();
    }

    public void setLowestCityDensity(Country country) {
        this.lowestCityDensity = getPopulationDensityStream(country).min().getAsInt();
    }

    public IntStream getPopulationDensityStream(Country country){
        return Arrays.stream(country.getStates())
                .flatMap(x -> Arrays.stream(x.getCities()))
                .mapToInt(x -> x.getPopulationDensity());
    }
}
