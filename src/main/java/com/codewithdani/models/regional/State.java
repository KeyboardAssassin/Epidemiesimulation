package com.codewithdani.models.regional;

import java.util.List;

import static com.codewithdani.models.actions.government.Restriction.CONTACT_RESTRICTIONS_VALUE;
import static com.codewithdani.models.actions.self.Distancing.SOCIAL_DISTANCING_VALUE;

public class State {
    private final String name;
    private final List<City> cities;
    private int totalPopulation;
    private double rValue;
    private double obedience;
    private double contactRestrictions = 1; // can only go higher e.g. 1 = 100% 2 = 50% 4 = 25%
    private int contactRestrictionsDaysLeft;
    private int stateInfectedPopulation;
    private double stateInfectionRatio = 0.0;

    public State(String name, List<City> citiesOfState) {
        this.name = name;
        this.cities = citiesOfState;
        this.stateInfectedPopulation = 0;
        this.obedience = 1;
    }

    public List<City> getCities() {
        return cities;
    }

    public String getName() {
        return name;
    }

    public double getSevenDaysIncidence(){
        double sumOfAllCityInfectionsPast7Days = 0;
        for (City city : cities){
            sumOfAllCityInfectionsPast7Days += city.getInfectionData().getTotalActiveCases();
        }
        return (sumOfAllCityInfectionsPast7Days / this.getTotalPopulation()) * 100000;
    }

    public double getObedience() {
        return obedience;
    }

    public void calculateAndSetInfectedPopulation(){
        int totalPeopleInfected = 0;
        for (City city: this.getCities()) {
            totalPeopleInfected += city.getInfectionData().getTotalActiveCases();
        }
        this.setStateInfectedPopulation(totalPeopleInfected);
    }

    public void setStateInfectedPopulation(int stateInfectedPopulation) {
        this.stateInfectedPopulation = stateInfectedPopulation;
    }

    public void calculateAndSetInfectionRatio(){
        this.stateInfectionRatio = this.stateInfectedPopulation / (double)this.totalPopulation ;
    }

    public void updateTotalPopulation() {
        this.totalPopulation =  this.getCities().stream().map(City::getPopulation).reduce(0, (Integer::sum));
    }

    public int getTotalPopulation() {
        return totalPopulation;
    }

    private void updateRValue(){
        double newRValue = 0;
        for (City city: cities) {
            newRValue += city.getInfectionData().getRValue() *  city.getPopulation() / getTotalPopulation();
        }
        this.rValue = newRValue;
    }

    public void updateState(){
        this.updateTotalPopulation();
        this.updateRValue();
    }

    public double getStateInfectionRatio() {
        return stateInfectionRatio;
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
            city.setContactRestrictions(restrictionValue);
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
            city.loseObedience(lost);
        }
    }

    public double getRValue() {
        return rValue;
    }

    public void activateContactRestrictions(int amountOfDays){
        this.setContactRestrictions(CONTACT_RESTRICTIONS_VALUE);
        this.setContactRestrictionDuration(amountOfDays);
        this.updateAllCitiesContactRestrictions(CONTACT_RESTRICTIONS_VALUE);
    }

    public void deactivateRestrictionsOnStateLevel(){
        setContactRestrictions(0);
        setContactRestrictionDuration(0);
    }

    public void resetStateAndEveryCityFromRestrictions(boolean socialDistancingActivated){
        int contactRestrictions = 0;
        if (socialDistancingActivated) contactRestrictions = SOCIAL_DISTANCING_VALUE;

        this.setContactRestrictionDuration(0);
        this.setContactRestrictions(contactRestrictions);
        this.updateAllCitiesContactRestrictions(contactRestrictions);
    }
}
