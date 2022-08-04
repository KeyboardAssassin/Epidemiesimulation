package com.codewithdani.api.models;

import com.codewithdani.models.regional.City;

public class CitySummaryTO {
    private String name;
    private final int population;
    private final int populationDensity;
    private final int infectedPeople;
    private final double rValue;
    private final double sevenDaysIncidence;

    public CitySummaryTO(City city) {
        this.name = city.getName();
        this.population = city.getPopulation();
        this.populationDensity = city.getPopulationDensity();
        this.infectedPeople = city.getInfectionData().getTotalNewCases();
        this.rValue = city.getInfectionData().getRValue();
        this.sevenDaysIncidence = city.getInfectionData().getSevenDaysIncidence();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public int getPopulationDensity() {
        return populationDensity;
    }

    public int getInfectedPeople() {
        return infectedPeople;
    }

    public double getRValue() {
        return rValue;
    }

    public double getSevenDaysIncidence() {
        return sevenDaysIncidence;
    }
}
