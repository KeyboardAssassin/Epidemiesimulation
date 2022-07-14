package com.codewithdani.models.summaries;

import com.codewithdani.models.regional.City;

public class CitySummary {
    private String name;
    private int population;
    private int populationDensity;
    private int infectedPeople;
    private double rValue;
    private double sevenDaysIncidence;

    public CitySummary(City city) {
        this.name = city.getName();
        this.population = city.getPopulation();
        this.populationDensity = city.getPopulationDensity();
        this.infectedPeople = city.getTotalNewCases();
        this.rValue = city.getRValue();
        this.sevenDaysIncidence = city.getSevenDaysIncidence();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
