package com.codewithdani.models.regional;

import com.codewithdani.util.SimulationUtils;
import com.codewithdani.models.actions.Measure;
import com.codewithdani.models.threats.Virus;

public class Country {
    private final String name;
    private State[] states;
    private Measure measure;

    private double obedience;
    private double incidence;
    private double rValue;
    private int newInfections;
    private int newDeathCases;
    private int countryTotalPopulation;
    private boolean socialDistancingActivated;
    private Virus currentVirus;
    public Country(String name, Virus startVirus) {
        this.name = name;
        this.measure = new Measure();
        this.currentVirus = startVirus;
    }

    public void setStates(State[] states) {
        this.states = states;
        this.setCountryTotalPopulation();
    }

    public String getName() {
        return name;
    }

    public int getAmountOfStates(){
        return states.length;
    }

    public State[] getStates() {
        return states;
    }

    public City getCityByName(String name){
        for (State state : states){
            for (City city : state.getCities()){
             if (city.getName().toLowerCase().equals(name)) return city;
            }
        }
        // TODO GEFAHR
        return null;
    }


    // TODO Ebenfalls
    public State getStateByName(String name){
        for (State state : states){
            if (state.getName().equalsIgnoreCase(name)) return state;
        }

        return null;
    }

    public Measure getMeasure() {
        return measure;
    }

    public double getIncidence() {
        return incidence;
    }

    public String getIncidenceAsString(){
        double totalIncidence = this.getIncidence();
        return SimulationUtils.convertIncidenceToStringWith2Digits(totalIncidence);
    }

    // TODO Redundant otra vez?
    public String getRValueAsString(){
        double rValue = this.getRValue();
        return SimulationUtils.convertIncidenceToStringWith2Digits(rValue);
    }
    public double getRValue() {
        return rValue;
    }

    public int getNewInfections() {
        return newInfections;
    }

    public int getNewDeathCases() {
        return newDeathCases;
    }

    public void setIncidence(double incidence) {
        this.incidence = incidence;
    }

    public void setNewInfections(int newInfections) {
        this.newInfections = newInfections;
    }

    public void setNewDeathCases(int newDeathCases) {
        this.newDeathCases = newDeathCases;
    }

    public void updateData(){
        try{
            setIncidence(this.calculateSummaryInfo("incidence"));
            setNewInfections((int)this.calculateSummaryInfo("newcases"));
            setNewDeathCases((int)this.calculateSummaryInfo("deadcases"));
            setObedience();
            this.updateRValue();
        }
        catch (Exception e){
            System.out.println("Request from frontend to update country but country is not yet set!");
        }
    }

    public double calculateSummaryInfo(String type){
        double sumOfAllSevenDaysIncidences = 0.0;
        int sumOfAllNewInfections = 0;
        int sumOfAllNewDeathCases = 0;
        int amountOfCities = 0;

        // loops through every state and every city
        for (State state: states) {
            for(City city : state.getCities()){
                sumOfAllSevenDaysIncidences += city.getInfectionData().getSevenDaysIncidence();
                // TODO richtige membervariable? Oder braucht es noch eine new cases
                sumOfAllNewInfections += city.getInfectionData().getFristInfectionNewCases();
                sumOfAllNewDeathCases += city.getInfectionData().getDeadCases();
            }
            amountOfCities += state.getCities().size();
        }

        // outputs the median value of a requested value
        return switch (type) {
            case "incidence" -> ((sumOfAllSevenDaysIncidences / amountOfCities) / this.getCountryTotalPopulation()) * 100000;
            case "newcases"  -> sumOfAllNewInfections;
            case "deadcases" -> sumOfAllNewDeathCases;
            default -> -1.0;
        };
    }

    private void updateRValue(){
        double rValue = 0;
            for (State state: this.getStates()) {
                rValue += state.getRValue() * state.getTotalPopulation() / this.getCountryTotalPopulation();
            }
        this.rValue = rValue;
    }


    private void setCountryTotalPopulation() {
        int totalPopulation = 0;

        for (State state: this.getStates()) {
            for (City city: state.getCities()) {
                totalPopulation += city.getPopulation();
            }
        }

        this.countryTotalPopulation = totalPopulation;
    }

    public int getCountryTotalPopulation() {
        return countryTotalPopulation;
    }

    public void setSocialDistancingActivated(boolean socialDistancingActivated) {
        this.socialDistancingActivated = socialDistancingActivated;
    }

    public boolean isSocialDistancingActivated() {
        return socialDistancingActivated;
    }

    public void setCurrentVirus(Virus currentVirus) {
        this.currentVirus = currentVirus;
        updateAllCityVirus(currentVirus);
    }

    public void updateAllCityVirus(Virus virus){
        for (State state: states) {
            for (City city: state.getCities()) {
                city.getInfectionData().setCurrentVirus(virus);
            }
        }
    }

    public double getObedience() {
        return obedience;
    }

    public void setObedience() {
        double totalObedience = 0;

        for (State state: this.getStates()) {
            totalObedience += state.getObedience();
        }
        this.obedience = totalObedience / states.length;
    }

}
