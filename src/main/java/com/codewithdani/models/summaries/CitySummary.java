package com.codewithdani.models.summaries;

public class CitySummary {
    private String name;
    private int population;
    private int populationDensity;
    private int infectedPeople;
    private double rValue;
    private double sevenDaysIncidence;

    public void setName(String name) {
        this.name = name;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setPopulationDensity(int populationDensity) {
        this.populationDensity = populationDensity;
    }

    public void setInfectedPeople(int infectedPeople) {
        this.infectedPeople = infectedPeople;
    }

    public void setrValue(double rValue) {
        this.rValue = rValue;
    }

    public void setSevenDaysIncidence(double sevenDaysIncidence) {
        this.sevenDaysIncidence = sevenDaysIncidence;
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

    public double getrValue() {
        return rValue;
    }

    public double getSevenDaysIncidence() {
        return sevenDaysIncidence;
    }
}
