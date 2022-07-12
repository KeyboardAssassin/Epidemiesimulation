package com.codewithdani.models.regional;

import java.text.DecimalFormat;

public class State {
    private final String name;
    private City[] cities;
    protected double obedience = 1;
    private int stateTotalPopulation;
    private int stateInfectedPopulation;
    private double stateInfectionRatio = 0.0;

    public State(String name, City[] citiesOfState) {
        this.name = name;
        this.cities = citiesOfState;
        this.stateTotalPopulation = this.calculateStatePopulation();
        this.stateInfectedPopulation = 0;
    }

    public void setCities(City[] cities) {
        this.cities = cities;
    }

    public City[] getCities() {
        return cities;
    }

    public String getName() {
        return name;
    }

    public String outputSevenDaysIncidenceAsString(){
        double totalIncidence = this.getSevenDaysIncidence();
        DecimalFormat df = new DecimalFormat("0.00");
        if (totalIncidence % 1 == 0) return  String.valueOf((int)totalIncidence); // TODO Smart?
        String resultIncidenceString = df.format(totalIncidence);

        return resultIncidenceString;
    }

    public double getSevenDaysIncidence(){
        double totalIncidence = 0;
        for (City city : cities){
            totalIncidence += city.getSevenDaysIncidence();
        }

        return totalIncidence / cities.length;
    }

    public double getObedience() {
        return obedience;
    }

    public void calculateAndSetInfectedPopulation(){
        int totalPeopleInfected = 0;
        for (City city: this.getCities()) {
            totalPeopleInfected += city.getTotalActiveCases();
        }
        this.setStateInfectedPopulation(totalPeopleInfected);
    }

    public void setStateInfectedPopulation(int stateInfectedPopulation) {
        this.stateInfectedPopulation = stateInfectedPopulation;
    }

    public void calculateAndSetInfectionRatio(){
        this.stateInfectionRatio = this.stateInfectedPopulation / this.stateTotalPopulation ;
    }

    /**
     * Calculates and sets the total population of a state
     *
     * @return total population of all states - e.g. 500000
     */
    public int calculateStatePopulation(){
        int totalPopulation = 0;
        for (City city: this.getCities()) {
            totalPopulation += city.getPopulation();
        }
        return totalPopulation;
    }

    public double getStateInfectionRatio() {
        return stateInfectionRatio;
    }
}
