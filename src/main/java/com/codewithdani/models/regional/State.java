package com.codewithdani.models.regional;

import java.util.Arrays;
import java.util.List;

public class State {
    private final String name;
    private final List<City> cities;
    private int totalPopulation;
    private double rValue;
    private double obedience = 1;
    private double contactRestrictions = 1; // can only go higher e.g. 1 = 100% 2 = 50% 4 = 25%
    private int contactRestrictionsDaysLeft;
    private final int stateTotalPopulation;
    private int stateInfectedPopulation;
    private double stateInfectionRatio = 0.0;

    public State(String name, List<City> citiesOfState) {
        this.name = name;
        this.cities = citiesOfState;
        this.stateTotalPopulation = this.calculateStatePopulation();
        this.stateInfectedPopulation = 0;
    }

    public List<City> getCities() {
        return cities;
    }

    public String getName() {
        return name;
    }

    public double getSevenDaysIncidence(){
        double totalIncidence = 0;
        for (City city : cities){
            totalIncidence += city.getSevenDaysIncidence();
        }
        double sevenDaysIncidence =  totalIncidence / cities.size();

        return (sevenDaysIncidence / this.getStateTotalPopulation()) * 100000;
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
        this.stateInfectionRatio = this.stateInfectedPopulation / (double)this.stateTotalPopulation ;
    }

    private void updateTotalPopulation() {
        this.totalPopulation =  this.getCities().stream().map(City::getPopulation).reduce(0, (Integer::sum));
    }

    public int getTotalPopulation() {
        return totalPopulation;
    }

    private void updateRValue(){
        double rValue = 0;
        for (City city: this.getCities()) {
            rValue += city.getRValue() *  city.getPopulation() / getTotalPopulation();
        }
        this.rValue = rValue;
    }

    public void updateState(){
        this.updateTotalPopulation();
        this.updateRValue();
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

    public int getStateTotalPopulation() {
        return stateTotalPopulation;
    }

    public void updateObedience() {
        double totalObedience = 0;

        for (City city: cities) {
            totalObedience += city.getObedience();
        }

        this.obedience = totalObedience / cities.size();
    }

    public double getContactRestrictions() {
        return contactRestrictions;
    }

    public void setContactRestrictions(double contactRestrictions) {
        this.contactRestrictions = contactRestrictions;
    }

    public void updateAllCitiesContactRestrictions(double restrictionValue){
        for (City city: cities) {
            city.setContactRestrictionsOfMotherState(restrictionValue);
            city.setContactRestrictionDuration(0);
        }
    }

    public void setContactRestrictionDuration(int amountOfDays){
        this.contactRestrictionsDaysLeft = amountOfDays;
    }

    public int getContactRestrictionsDaysLeft() {
        return contactRestrictionsDaysLeft;
    }

    public void removeObedienceFromAllCities(double lost){
        for (City city: cities) {
            city.looseObedience(lost);
        }
    }

    public double getrValue() {
        return rValue;
    }
}
